package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.DownloadIntegrationBaseTest;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BucketIntegrationTest extends DownloadIntegrationBaseTest {

    private static final String BUCKET_URI_BY_COUNTRY = "/bucket/country";

    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void test_Bucket_Get_ShouldReturn_200Response_And_ListOfCountryNames_WhenRequestedFor_AllAvailableCountries() throws Exception {
        super.initBuckets(false);

        MvcResult mvcResult = null;
        int expectedCountryCount = landCollection.size();

        mvcResult = this.mockMvc.perform(get(BUCKET_URI_BY_COUNTRY))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(expectedCountryCount, om.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Set<String>>() { }).size());
        Assert.assertEquals(landCollection, om.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Set<String>>() { }));

        super.destroyBuckets(false);
    }

    @Test
    public void test_Bucket_Get_ShouldReturn_200Response_And_EmptyList_WhenRequestedFor_AllAvailableCountries_ButNoCountry_IsAvailable() throws Exception {
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(get(BUCKET_URI_BY_COUNTRY))
                .andDo(print())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assert.assertEquals(0, om.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Set<String>>() { }).size());
        Assert.assertEquals(new TreeSet<>(), om.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Set<String>>() { }));
    }
}