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
import edu.upc.gessi.glidebackend.entity.IndividualPlayerEntity;
import edu.upc.gessi.glidebackend.entity.StudentUserEntity;
import edu.upc.gessi.glidebackend.repository.IndividualPlayerRepository;
import edu.upc.gessi.glidebackend.service.impl.GamificationServiceImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;

class GamificationServiceUnitTests {
    @InjectMocks
    private GamificationServiceImpl gamificationService;

    @Mock
    private IndividualPlayerRepository individualPlayerRepository;

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

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("playername", mockPlayername);
        mockResponse.put("points", 0);
        mockResponse.put("level", 1);

        IndividualPlayerEntity mockEntity = new IndividualPlayerEntity();
        StudentUserEntity student = new StudentUserEntity();
        student.setNickname("mockNickname");
        mockEntity.setStudentUserEntity(student);

        when(restTemplate.getForObject(mockUri, Map.class)).thenReturn(mockResponse);
        when(individualPlayerRepository.findById(mockPlayername)).thenReturn(Optional.of(mockEntity));

        Object result = gamificationService.getIndividualPlayer(mockPlayername);

        assertTrue(result instanceof Map);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertEquals("mockNickname", resultMap.get("nickname"));
        assertEquals(mockPlayername, resultMap.get("playername"));
        assertEquals(0, resultMap.get("points"));

        verify(restTemplate).getForObject(mockUri, Map.class);
    }


    @Test
    void testGetIndividualPlayer_Failure() {
        String mockPlayername = "player123";
        String mockUri = gamificationAPIBaseURL + "/players/individuals/" + mockPlayername;

        when(restTemplate.getForObject(mockUri, Map.class)).thenThrow(new RuntimeException("Error"));

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
