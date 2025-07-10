package edu.upc.gessi.glidebackend.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MetricServiceUnitTests {
    @InjectMocks
    private MetricServiceImpl metricService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${gamification.api.base-url}")
    private String gamificationAPIBaseURL;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            Field restTemplateField = MetricServiceImpl.class.getDeclaredField("restTemplate");
            restTemplateField.setAccessible(true);
            restTemplateField.set(metricService, restTemplate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllCategories_success() {
        String uri = gamificationAPIBaseURL + "/metrics/categories";
        Object[] mockCategories = new Object[]{new Object()};
        when(restTemplate.getForObject(uri, Object[].class)).thenReturn(mockCategories);

        List<Object> categories = metricService.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(mockCategories[0], categories.getFirst());
    }

    @Test
    void testGetAllCategories_error() {
        String uri = gamificationAPIBaseURL + "/metrics/categories";
        when(restTemplate.getForObject(uri, Object[].class)).thenThrow(new RuntimeException("Error"));

        List<Object> categories = metricService.getAllCategories();

        assertNotNull(categories);
        assertInstanceOf(ResponseEntity.class, categories.getFirst());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) categories.getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error!, Please try again", responseEntity.getBody());
    }

    @Test
    void testGetProjectCategories_success() {
        String prj = "project1";
        String uri = gamificationAPIBaseURL + "/metrics?prj=" + prj;
        Object[] mockProjectCategories = new Object[]{new Object()};
        when(restTemplate.getForObject(uri, Object[].class)).thenReturn(mockProjectCategories);

        List<Object> categories = metricService.getProjectCategories(prj);

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(mockProjectCategories[0], categories.getFirst());
    }

    @Test
    void testGetProjectCategories_error() {
        String prj = "project1";
        String uri = gamificationAPIBaseURL + "/metrics?prj=" + prj;
        when(restTemplate.getForObject(uri, Object[].class)).thenThrow(new RuntimeException("Error"));

        List<Object> categories = metricService.getProjectCategories(prj);

        assertNotNull(categories);
        assertInstanceOf(ResponseEntity.class, categories.getFirst());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) categories.getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error!, Please try again", responseEntity.getBody());
    }

    @Test
    void testGetMetrics_success() {
        String prj = "project1";
        String uri = gamificationAPIBaseURL + "/metrics/students?prj=" + prj;
        Object[] mockMetrics = new Object[]{new Object()};
        when(restTemplate.getForObject(uri, Object[].class)).thenReturn(mockMetrics);

        List<Object> metrics = metricService.getMetrics(prj);

        assertNotNull(metrics);
        assertEquals(1, metrics.size());
        assertEquals(mockMetrics[0], metrics.getFirst());
    }

    @Test
    void testGetMetrics_error() {
        String prj = "project1";
        String uri = gamificationAPIBaseURL + "/metrics/students?prj=" + prj;
        when(restTemplate.getForObject(uri, Object[].class)).thenThrow(new RuntimeException("Error"));

        List<Object> metrics = metricService.getMetrics(prj);

        assertNotNull(metrics);
        assertInstanceOf(ResponseEntity.class, metrics.getFirst());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) metrics.getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error!, Please try again", responseEntity.getBody());
    }

    @Test
    void testGetMetricsHistory_success() {
        String prj = "project1";
        String from = "2023-01-01";
        String to = "2023-12-31";
        String uri = gamificationAPIBaseURL + "/metrics/students/historical?prj=" + prj + "&from=" + from + "&to=" + to;
        Object[] mockMetrics = new Object[]{new Object()};
        when(restTemplate.getForObject(uri, Object[].class)).thenReturn(mockMetrics);

        List<Object> metrics = metricService.getMetricsHistory(prj, from, to);

        assertNotNull(metrics);
        assertEquals(1, metrics.size());
        assertEquals(mockMetrics[0], metrics.getFirst());
    }

    @Test
    void testGetMetricsHistory_error() {
        String prj = "project1";
        String from = "2023-01-01";
        String to = "2023-12-31";
        String uri = gamificationAPIBaseURL + "/metrics/students/historical?prj=" + prj + "&from=" + from + "&to=" + to;
        when(restTemplate.getForObject(uri, Object[].class)).thenThrow(new RuntimeException("Error"));

        List<Object> metrics = metricService.getMetricsHistory(prj, from, to);

        assertNotNull(metrics);
        assertInstanceOf(ResponseEntity.class, metrics.getFirst());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) metrics.getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error!, Please try again", responseEntity.getBody());
    }
}
