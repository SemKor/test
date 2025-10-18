package com.semkor.test.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testIndexPage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testNotFoundPage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/page/unknown", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }




}
