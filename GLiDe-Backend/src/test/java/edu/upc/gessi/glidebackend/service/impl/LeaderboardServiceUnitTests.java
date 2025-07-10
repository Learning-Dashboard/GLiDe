package edu.upc.gessi.glidebackend.service.impl;
import edu.upc.gessi.glidebackend.repository.IndividualPlayerRepository;
import edu.upc.gessi.glidebackend.service.LeaderboardService;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class LeaderboardServiceUnitTests {
    @InjectMocks
    private LeaderboardServiceImpl leaderboardService;

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
            Field restTemplateField = LeaderboardServiceImpl.class.getDeclaredField("restTemplate");
            restTemplateField.setAccessible(true);
            restTemplateField.set(leaderboardService, restTemplate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetLeaderboard_success() {
        String uri = gamificationAPIBaseURL + "/leaderboards/" + 1L;
        Object mockLeaderboard = new Object();
        when(restTemplate.getForObject(uri, Object.class)).thenReturn(mockLeaderboard);

        Object result = leaderboardService.getLeaderboard(1L);

        assertNotNull(result);
        assertEquals(mockLeaderboard, result);
    }

    @Test
    void testGetLeaderboard_error() {
        String uri = gamificationAPIBaseURL + "/leaderboards/" + 1L;
        when(restTemplate.getForObject(uri, Object.class)).thenThrow(new RuntimeException("Error"));

        Object result = leaderboardService.getLeaderboard(1L);

        assertNotNull(result);
        assertInstanceOf(List.class, result);

        ResponseEntity<?> responseEntity = (ResponseEntity<?>) ((List<?>) result).getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error!, Please try again", responseEntity.getBody());
    }

    @Test
    void testGetLeaderboardResults_success() {
        String uri = gamificationAPIBaseURL + "/leaderboards/" + 1L + "/results";

        Map<String, Object> mockLeaderboard = new HashMap<>();
        List<Map<String, Object>> mockPlayers = new ArrayList<>();
        Map<String, Object> player = new HashMap<>();
        player.put("playername", "test.user");
        mockPlayers.add(player);
        mockLeaderboard.put("leaderboardResults", mockPlayers);

        Object[] mockResults = new Object[]{mockLeaderboard};

        when(restTemplate.getForObject(uri, Object[].class)).thenReturn(mockResults);
        when(individualPlayerRepository.findById("test.user")).thenReturn(Optional.empty()); // No nickname per simplificar

        List<Object> results = leaderboardService.getLeaderboardResults(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Map);
        Map<?, ?> resultMap = (Map<?, ?>) results.get(0);
        assertTrue(resultMap.containsKey("leaderboardResults"));
    }


    @Test
    void testGetLeaderboardResults_error() {
        String uri = gamificationAPIBaseURL + "/leaderboards/" + 1L + "/results";
        when(restTemplate.getForObject(uri, Object[].class)).thenThrow(new RuntimeException("Error"));

        List<Object> results = leaderboardService.getLeaderboardResults(1L);

        assertNotNull(results);
        assertInstanceOf(ResponseEntity.class, results.getFirst());

        ResponseEntity<?> responseEntity = (ResponseEntity<?>) results.getFirst();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error!, Please try again", responseEntity.getBody());
    }
}
