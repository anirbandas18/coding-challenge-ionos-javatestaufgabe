package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.AuftraegeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.AuftraegeEntityPrimaryKey;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.KundeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering.FilteringProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering.FilteringReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering.FilteringWriter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping.MappingProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping.MappingReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping.MappingWriter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation.SegmentationProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation.SegmentationReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation.SegmentationWriter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.upload.UploadProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.upload.UploadReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.upload.UploadWriter;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest
//@ContextConfiguration(classes = { SynchronizationBatchConfiguration.class })
public class SynchronizationBatchIntegrationTest {

    private EntityManager em;
    private JobLauncherTestUtils jobLauncherTestUtils;
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    private Job synchronizationJob;
    private FilteringReader filteringReader;
    private FilteringProcessor filteringProcessor;
    private FilteringWriter filteringWriter;
    private MappingReader mappingreader;
    private MappingProcessor mappingProcessor;
    private MappingWriter mappingWriter;
    private SegmentationReader segmentationReader;
    private SegmentationProcessor segmentationProcessor;
    private SegmentationWriter segmentationWriter;
    private UploadReader uploadReader;
    private UploadProcessor uploadProcessor;
    private UploadWriter uploadWriter;

    private Integer redisPort;
    private String timestampFormat;
    private String timezone;
    private String jobParameterName1;

    private RedisServer redisServer;
    private DateTimeFormatter dtf;

    private KundeEntity kundeEntity1;
    private KundeEntity kundeEntity2;

    private AuftraegeEntity auftraegeEntity1;
    private AuftraegeEntity auftraegeEntity2;
    private AuftraegeEntity auftraegeEntity3;
    private AuftraegeEntity auftraegeEntity4;

    @Value("${spring.redis.port}")
    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    @Value("${s3export.sync.job.timestamp.format}")
    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    @Value("${s3export.seed.timezone:Europe/Paris}")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Value("${s3export.sync.job.parameter.1:synchronization-timestamp}")
    public void setJobParameterName1(String jobParameterName1) {
        this.jobParameterName1 = jobParameterName1;
    }

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Autowired
    public void setJobLauncherTestUtils(JobLauncherTestUtils jobLauncherTestUtils) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
    }

    @Autowired
    public void setJobRepositoryTestUtils(JobRepositoryTestUtils jobRepositoryTestUtils) {
        this.jobRepositoryTestUtils = jobRepositoryTestUtils;
    }

    @Autowired
    public void setSynchronizationJob(Job synchronizationJob) {
        this.synchronizationJob = synchronizationJob;
    }


    @Autowired
    public void setFilteringReader(FilteringReader filteringReader) {
        this.filteringReader = filteringReader;
    }

    @Autowired
    public void setFilteringProcessor(FilteringProcessor filteringProcessor) {
        this.filteringProcessor = filteringProcessor;
    }

    @Autowired
    public void setFilteringWriter(FilteringWriter filteringWriter) {
        this.filteringWriter = filteringWriter;
    }

    @Autowired
    public void setMappingreader(MappingReader mappingreader) {
        this.mappingreader = mappingreader;
    }

    @Autowired
    public void setMappingProcessor(MappingProcessor mappingProcessor) {
        this.mappingProcessor = mappingProcessor;
    }

    @Autowired
    public void setMappingWriter(MappingWriter mappingWriter) {
        this.mappingWriter = mappingWriter;
    }

    @Autowired
    public void setSegmentationReader(SegmentationReader segmentationReader) {
        this.segmentationReader = segmentationReader;
    }

    @Autowired
    public void setSegmentationProcessor(SegmentationProcessor segmentationProcessor) {
        this.segmentationProcessor = segmentationProcessor;
    }

    @Autowired
    public void setSegmentationWriter(SegmentationWriter segmentationWriter) {
        this.segmentationWriter = segmentationWriter;
    }

    @Autowired
    public void setUploadReader(UploadReader uploadReader) {
        this.uploadReader = uploadReader;
    }

    @Autowired
    public void setUploadProcessor(UploadProcessor uploadProcessor) {
        this.uploadProcessor = uploadProcessor;
    }

    @Autowired
    public void setUploadWriter(UploadWriter uploadWriter) {
        this.uploadWriter = uploadWriter;
    }


    private String getJobTimestampInUTC() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoneDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of(timezone));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(timestampFormat);
        String timestamp = utcZoneDateTime.format(dtf);
        return timestamp;
    }

    private JobParameters getJobParameters() {
        Instant synchronizationInstant = Instant.now();
        Long synchronizationJobStartTime = synchronizationInstant.toEpochMilli();;
        JobParameters synchronizationJobParameters =   new JobParametersBuilder()
                .addLong(jobParameterName1, synchronizationJobStartTime).toJobParameters();
        return synchronizationJobParameters;
    }

    @BeforeAll
    private void setUp() {
        redisServer = RedisServer.builder().port(redisPort).setting("maxmemory 128M").build();

        dtf = DateTimeFormatter.ofPattern(timestampFormat);

        kundeEntity1 = new KundeEntity();
        kundeEntity1.setLand("land 1");
        kundeEntity1.setEmail("kunde1@test.com");
        kundeEntity1.setOrt("ort 1");
        kundeEntity1.setNachName("nach 1");
        kundeEntity1.setVorName("kunde 1");
        kundeEntity1.setPlz("plz 1");
        kundeEntity1.setStrasse("strasse 1");
        kundeEntity1.setFirmenName("firma 1");
        kundeEntity1.setStrassenZuSatz("strassenzusatz 1");

        kundeEntity2 = new KundeEntity();
        kundeEntity2.setLand("land 2");
        kundeEntity2.setEmail("kunde1@test.com");
        kundeEntity2.setOrt("ort 2");
        kundeEntity2.setNachName("nach 2");
        kundeEntity2.setVorName("kunde 2");
        kundeEntity2.setPlz("plz 2");
        kundeEntity2.setStrasse("strasse 2");
        kundeEntity2.setFirmenName("firma 2");
        kundeEntity2.setStrassenZuSatz("strassenzusatz 2");

        auftraegeEntity1 = new AuftraegeEntity();
        AuftraegeEntityPrimaryKey pk1 = new AuftraegeEntityPrimaryKey();
        pk1.setAuftragId(String.valueOf(System.nanoTime()));
        pk1.setArtikelNummber(UUID.randomUUID().toString());
        auftraegeEntity1.setId(pk1);
        auftraegeEntity1.setCreated(getJobTimestampInUTC());
        auftraegeEntity1.setLastChange(getJobTimestampInUTC());

        auftraegeEntity2 = new AuftraegeEntity();
        AuftraegeEntityPrimaryKey pk2 = new AuftraegeEntityPrimaryKey();
        pk2.setAuftragId(String.valueOf(System.nanoTime()));
        pk2.setArtikelNummber(UUID.randomUUID().toString());
        auftraegeEntity2.setId(pk2);
        auftraegeEntity2.setCreated(getJobTimestampInUTC());
        auftraegeEntity2.setLastChange(getJobTimestampInUTC());

        auftraegeEntity3 = new AuftraegeEntity();
        AuftraegeEntityPrimaryKey pk3 = new AuftraegeEntityPrimaryKey();
        pk3.setAuftragId(String.valueOf(System.nanoTime()));
        pk3.setArtikelNummber(UUID.randomUUID().toString());
        auftraegeEntity3.setId(pk1);
        auftraegeEntity3.setCreated(getJobTimestampInUTC());
        auftraegeEntity3.setLastChange(getJobTimestampInUTC());

        auftraegeEntity4 = new AuftraegeEntity();
        AuftraegeEntityPrimaryKey pk4 = new AuftraegeEntityPrimaryKey();
        pk4.setAuftragId(String.valueOf(System.nanoTime()));
        pk4.setArtikelNummber(UUID.randomUUID().toString());
        auftraegeEntity4.setId(pk1);
        auftraegeEntity4.setCreated(getJobTimestampInUTC());
        auftraegeEntity4.setLastChange(getJobTimestampInUTC());
    }

    @AfterAll
    private void tearDown() {
        if(redisServer != null) {
            redisServer.stop();
        }
        if(jobRepositoryTestUtils != null) {
            jobRepositoryTestUtils.removeJobExecutions();
        }
    }

    @BeforeEach
    private void init() {
        kundeEntity1 = em.merge(kundeEntity1);
        kundeEntity2 = em.merge(kundeEntity2);

        auftraegeEntity1.getId().setKundenId(String.valueOf(kundeEntity1.getKundenId()));
        auftraegeEntity2.getId().setKundenId(String.valueOf(kundeEntity1.getKundenId()));
        auftraegeEntity3.getId().setKundenId(String.valueOf(kundeEntity2.getKundenId()));
        auftraegeEntity4.getId().setKundenId(String.valueOf(kundeEntity2.getKundenId()));

        em.merge(auftraegeEntity1);
        em.merge(auftraegeEntity2);
        em.merge(auftraegeEntity3);
        em.merge(auftraegeEntity4);
    }

    @AfterEach
    private void destroy() {
        auftraegeEntity1.getId().setKundenId(null);
        auftraegeEntity2.getId().setKundenId(null);
        auftraegeEntity3.getId().setKundenId(null);
        auftraegeEntity4.getId().setKundenId(null);

        em.remove(auftraegeEntity1);
        em.remove(auftraegeEntity2);
        em.remove(auftraegeEntity3);
        em.remove(auftraegeEntity4);

        em.remove(kundeEntity1);
        em.remove(kundeEntity2);
    }

    @Test
    public void test_JobExecution_ShouldCreate_FilesOnFileSystem_AndUploadThen_ToRemoteBucket() {

    }

    /*@Test
    public void test_SynchronizationJob_StepExecution_Filtering_FilterReader() {
        StepExecution stepExecution = MetaDataInstanceFactory
                .createStepExecution(getJobParameters());

        // when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            BookRecord bookRecord;
            itemReader.open(stepExecution.getExecutionContext());
            while ((bookRecord = itemReader.read()) != null) {

                // then
                assertThat(bookRecord.getBookName(), is("Foundation"));
                assertThat(bookRecord.getBookAuthor(), is("Asimov I."));
                assertThat(bookRecord.getBookISBN(), is("ISBN 12839"));
                assertThat(bookRecord.getBookFormat(), is("hardcover"));
                assertThat(bookRecord.getPublishingYear(), is("2018"));
            }
            itemReader.close();
            return null;
        });
    }

    @Test
    public void test_Operation_Post_ShouldReturn_201Response_And_NewOperationId_WhenPosted_WithValidOperationForm() throws Exception {
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(post(OPERATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(operationForm)))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        Assert.assertTrue(StringUtils.hasText(mvcResult.getResponse().getContentAsString()));
    }*/

}