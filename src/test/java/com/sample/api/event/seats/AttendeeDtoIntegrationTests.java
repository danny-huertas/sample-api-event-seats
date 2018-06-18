package com.sample.api.event.seats;

import com.sample.api.EventSeatsApplication;
import com.sample.api.event.seats.configuration.SpringRootConfig;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventSeatsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AttendeeDtoIntegrationTests {

    @LocalServerPort
    private int port;
    private Map<String, Object> requestParams;

    @BeforeClass
    public static void initClass() {
        RestAssured.basePath = SpringRootConfig.REQUEST_MAPPING;
    }

    @Before
    public void initTest() {
        requestParams = new LinkedHashMap<>();
    }

    @Test
    public void attendeeTest() {
        //TODO: Add attendee integration tests
    }
}
