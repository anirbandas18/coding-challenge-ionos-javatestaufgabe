package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.DownloadIntegrationBaseTest;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import io.minio.errors.MinioException;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class FileIntegrationTest extends DownloadIntegrationBaseTest {

    private static final String FILE_URI_BY_COUNTRY = "/file/country/{country}";
    private static final String FILE_URI_BY_COUNTRY_DATE = "/file/country/{country}/date/{date}";

    private MockMvc mockMvc;

    private String userSeqHeaderName;
    private Long userSeqHeaderValue;
    private String dateFormat;
    private Map<String,List<String>> bucketObjectMap;

    @Value("${s3export.download.job.bucket.timestamp.format}")
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Value("${s3export.download.file.header.name.userseq}")
    public void setUserSeqHeaderName(String userSeqHeaderName) {
        this.userSeqHeaderName = userSeqHeaderName;
    }

    @Value("${s3export.download.file.header.value.userseq}")
    public void setUserSeqHeaderValue(Long userSeqHeaderValue) {
        this.userSeqHeaderValue = userSeqHeaderValue;
    }

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    private void init() throws CsvRequiredFieldEmptyException, MinioException, CsvDataTypeMismatchException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        bucketObjectMap = super.initBuckets(true);
    }

    @AfterEach
    private void destroy() throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        super.destroyBuckets(true);
    }

    @Test
    public void test_File_Get_ShouldReturn_200Response_And_LatestCSVFile_ForCountry_WhenRequestedFor_ASpecificCountry_ByAValidUserSeq() throws Exception {
        MvcResult mvcResult = null;
        List<String> countryNames = new ArrayList<>(bucketObjectMap.keySet());
        Collections.shuffle(countryNames);
        String countryName = countryNames != null && !countryNames.isEmpty() ? countryNames.get(0) : "";
        HttpHeaders headers = new HttpHeaders();
        headers.add(userSeqHeaderName, userSeqHeaderValue.toString());


        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY, countryName).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assert.assertNotNull(mvcResult.getResponse().getContentAsString());
        Assert.assertNotEquals(0, mvcResult.getResponse().getContentLength());
        Assert.assertEquals(MediaType.TEXT_PLAIN_VALUE, mvcResult.getResponse().getContentType());
        Assert.assertTrue(mvcResult.getResponse().containsHeader(HttpHeaders.CONTENT_DISPOSITION));
        Assert.assertTrue(mvcResult.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION).contains(countryName));
    }

    @Test
    public void test_File_Get_ShouldReturn_400Response_And_ErrorCode_S3E_DOWNLOAD_001_WhenRequestedBy_EmptyCountry_ByAValidUserSeq() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "country";
        String country = " ";
        HttpHeaders headers = new HttpHeaders();
        headers.add(userSeqHeaderName, userSeqHeaderValue.toString());

        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY, country).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assert.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_File_Get_ShouldReturn_404Response_And_ErrorCode_S3E_DOWNLOAD_002_WhenRequestedBy_AbsentCountry_ByAValidUserSeq() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = DownloadErrorCode.DOWNLOAD_NOT_FOUND.getErrorCode();
        String fieldName = "country";
        String country = "1234";
        HttpHeaders headers = new HttpHeaders();
        headers.add(userSeqHeaderName, userSeqHeaderValue.toString());

        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY, country).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assert.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @ParameterizedTest
    @ValueSource(strings = { " ", "-1", "0" })
    public void test_File_Get_ShouldReturn_400Response_And_ErrorCode_S3E_DOWNLOAD_001_WhenRequestedBy_AValidCountryAnd_BlankNegativeZeroUserSequence(String userSeq) throws Exception {
        MvcResult mvcResult = null;
        String errorCode = DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = userSeqHeaderName;
        String country = "canada";
        HttpHeaders headers = new HttpHeaders();
        headers.add(userSeqHeaderName, userSeq);

        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY, country).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assert.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_File_Get_ShouldReturn_200Response_And_LatestCSVFile_ForCountry_WhenRequestedFor_ASpecificCountryAndDate_ByAValidUserSeq() throws Exception {
        MvcResult mvcResult = null;
        List<String> countryNames = new ArrayList<>(bucketObjectMap.keySet());
        Collections.shuffle(countryNames);
        String countryName = countryNames != null && !countryNames.isEmpty() ? countryNames.get(0) : "";
        String date = DateTimeFormatter.ofPattern(dateFormat).format(LocalDate.now());
        HttpHeaders headers = new HttpHeaders();
        headers.add(userSeqHeaderName, userSeqHeaderValue.toString());


        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY_DATE, countryName, date).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assert.assertNotNull(mvcResult.getResponse().getContentAsString());
        Assert.assertNotEquals(0, mvcResult.getResponse().getContentLength());
        Assert.assertEquals(MediaType.TEXT_PLAIN_VALUE, mvcResult.getResponse().getContentType());
        Assert.assertTrue(mvcResult.getResponse().containsHeader(HttpHeaders.CONTENT_DISPOSITION));
        Assert.assertTrue(mvcResult.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION).contains(countryName));
    }

    @Test
    public void test_File_Get_ShouldReturn_400Response_And_ErrorCode_S3E_DOWNLOAD_001_WhenRequestedBy_EmptyCountry_AndSpecificDate_ByAValidUserSeq() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "country";
        String country = " ";
        String date = DateTimeFormatter.ofPattern(dateFormat).format(LocalDate.now());
        HttpHeaders headers = new HttpHeaders();
        headers.add(userSeqHeaderName, userSeqHeaderValue.toString());

        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY_DATE, country, date).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assert.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_File_Get_ShouldReturn_404Response_And_ErrorCode_S3E_DOWNLOAD_002_WhenRequestedBy_NotACountry_AndSpecificDate_ByAValidUserSeq() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = DownloadErrorCode.DOWNLOAD_NOT_FOUND.getErrorCode();
        String fieldName = "country";
        String country = "1234";
        String date = DateTimeFormatter.ofPattern(dateFormat).format(LocalDate.now());
        HttpHeaders headers = new HttpHeaders();
        headers.add(userSeqHeaderName, userSeqHeaderValue.toString());

        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY_DATE, country, date).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assert.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @ParameterizedTest
    @ValueSource(strings = { "-1", "qwerty", "2021-22-08" })
    public void test_File_Get_ShouldReturn_404Response_And_ErrorCode_S3E_DOWNLOAD_003_WhenRequestedBy_AValidCountry_InvalidNonComplaintPastDate_ByAValidUserSeq(String date) throws Exception {
        MvcResult mvcResult = null;
        String errorCode = DownloadErrorCode.DOWNLOAD_NOT_FOUND.getErrorCode();
        String fieldName = "date";
        String country = "germany";
        HttpHeaders headers = new HttpHeaders();
        headers.add(userSeqHeaderName, userSeqHeaderValue.toString());

        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY_DATE, country, date).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assert.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_File_Get_ShouldReturn_400Response_And_ErrorCode_S3E_DOWNLOAD_002_WhenRequestedBy_AValidCountry_AbsentDate_ByAValidUserSeq() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = DownloadErrorCode.DOWNLOAD_NOT_FOUND.getErrorCode();
        String fieldName = "date";
        String country = "germany";
        HttpHeaders headers = new HttpHeaders();
        String date = DateTimeFormatter.ofPattern(dateFormat).format(LocalDate.of(2020, 01, 22));
        headers.add(userSeqHeaderName, userSeqHeaderValue.toString());

        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY_DATE, country, date).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assert.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @ParameterizedTest
    @ValueSource(strings = { " ", "-1", "0" })
    public void test_File_Get_ShouldReturn_400Response_And_ErrorCode_S3E_DOWNLOAD_001_WhenRequestedBy_AValidCountryAndSpecificDate_BlankNegativeZeroUserSequence(String userSeq) throws Exception {
        MvcResult mvcResult = null;
        String errorCode = DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = userSeqHeaderName;
        String country = "germany";
        String date = DateTimeFormatter.ofPattern(dateFormat).format(LocalDate.now());
        HttpHeaders headers = new HttpHeaders();
        headers.add(userSeqHeaderName, userSeq);

        mvcResult = this.mockMvc.perform(get(FILE_URI_BY_COUNTRY_DATE, country, date).headers(headers))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assert.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }
}