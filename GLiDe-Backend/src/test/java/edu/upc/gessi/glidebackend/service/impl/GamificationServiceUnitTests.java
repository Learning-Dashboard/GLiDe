package edu.upc.gessi.glidebackend.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GamificationServiceUnitTests {
    @InjectMocks
    private GamificationServiceImpl gamificationService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${gamification.api.base-url}")
    private String gamificationAPIBaseURL;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        try {
            Field restTemplateField = GamificationServiceImpl.class.getDeclaredField("restTemplate");
            restTemplateField.setAccessible(true);
            restTemplateField.set(gamificationService, restTemplate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetIndividualPlayer_Success() {
        String mockPlayername = "player123";
        String mockUri = gamificationAPIBaseURL + "/players/individuals/" + mockPlayername;
        Object mockResponse = new Object();

        when(restTemplate.getForObject(mockUri, Object.class)).thenReturn(mockResponse);

        Object result = gamificationService.getIndividualPlayer(mockPlayername);
        System.out.println(result);

        assertEquals(mockResponse, result);
        Mockito.verify(restTemplate).getForObject(mockUri, Object.class);
    }

    @Test
    void testGetIndividualPlayer_Failure() {
        String mockPlayername = "player123";
        String mockUri = gamificationAPIBaseURL + "/players/individuals/" + mockPlayername;

        when(restTemplate.getForObject(mockUri, Object.class)).thenThrow(new RuntimeException("Error"));

        Object result = gamificationService.getIndividualPlayer(mockPlayername);

        assertInstanceOf(List.class, result);
        assertInstanceOf(ResponseEntity.class, ((List<?>) result).getFirst());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) ((List<?>) result).getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void testGetTeamPlayer_Success() {
        String mockPlayername = "team123";
        String mockUri = gamificationAPIBaseURL + "/players/teams/" + mockPlayername;
        Object mockResponse = new Object();

        when(restTemplate.getForObject(mockUri, Object.class)).thenReturn(mockResponse);

        Object result = gamificationService.getTeamPlayer(mockPlayername);

        assertEquals(mockResponse, result);
        Mockito.verify(restTemplate).getForObject(mockUri, Object.class);
    }

    @Test
    void testGetTeamPlayer_Failure() {
        String mockPlayername = "team123";
        String mockUri = gamificationAPIBaseURL + "/players/teams/" + mockPlayername;

        when(restTemplate.getForObject(mockUri, Object.class)).thenThrow(new RuntimeException("Error"));

        Object result = gamificationService.getTeamPlayer(mockPlayername);

        assertInstanceOf(List.class, result);
        assertInstanceOf(ResponseEntity.class, ((List<?>) result).getFirst());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) ((List<?>) result).getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void testGetPlayerAchievements_Success() {
        String mockPlayername = "player123";
        String attained = "true";
        String category = "category1";
        String mockUri = gamificationAPIBaseURL + "/players/" + mockPlayername + "/achievements?attained=" + attained + "&category=" + category;
        Object[] mockResponse = new Object[]{"achievement1", "achievement2"};

        when(restTemplate.getForObject(mockUri, Object[].class)).thenReturn(mockResponse);

        List<Object> result = gamificationService.getPlayerAchievements(mockPlayername, attained, category);

        assertEquals(2, result.size());
        assertEquals("achievement1", result.get(0));
        assertEquals("achievement2", result.get(1));
        Mockito.verify(restTemplate).getForObject(mockUri, Object[].class);
    }

    @Test
    void testGetPlayerAchievements_Failure() {
        String mockPlayername = "player123";
        String attained = "true";
        String category = "category1";
        String mockUri = gamificationAPIBaseURL + "/players/" + mockPlayername + "/achievements?attained=" + attained + "&category=" + category;

        when(restTemplate.getForObject(mockUri, Object[].class)).thenThrow(new RuntimeException("Error"));

        List<Object> result = gamificationService.getPlayerAchievements(mockPlayername, attained, category);

        assertEquals(1, result.size());
        assertInstanceOf(ResponseEntity.class, result.getFirst());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result.getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void testGetEvaluableActions_Success() {
        String mockUri = gamificationAPIBaseURL + "/evaluableActions";
        Object[] mockResponse = new Object[]{"action1", "action2"};

        when(restTemplate.getForObject(mockUri, Object[].class)).thenReturn(mockResponse);

        List<Object> result = gamificationService.getEvaluableActions();

        assertEquals(2, result.size());
        assertEquals("action1", result.get(0));
        assertEquals("action2", result.get(1));
        Mockito.verify(restTemplate).getForObject(mockUri, Object[].class);
    }

    @Test
    void testGetEvaluableActions_Failure() {
        String mockUri = gamificationAPIBaseURL + "/evaluableActions";

        when(restTemplate.getForObject(mockUri, Object[].class)).thenThrow(new RuntimeException("Error"));

        List<Object> result = gamificationService.getEvaluableActions();

        assertEquals(1, result.size());
        assertInstanceOf(ResponseEntity.class, result.getFirst());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result.getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

}
