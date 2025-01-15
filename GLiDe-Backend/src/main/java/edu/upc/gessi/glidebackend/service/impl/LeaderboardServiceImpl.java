package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gamification.api.base-url}")
    private String gamificationAPIBaseURL;

    public List<Object> getLeaderboards(String gameSubjectAcronym, Integer gameCourse, String gamePeriod){
        try{
            String uri = gamificationAPIBaseURL + "/leaderboards?gameSubjectAcronym=" + gameSubjectAcronym + "&gameCourse=" + gameCourse + "&gamePeriod=" + gamePeriod;
            Object[] leaderboards = this.restTemplate.getForObject(uri, Object[].class);
            return Arrays.asList(leaderboards);
        } catch (Exception e){
            e.printStackTrace();
            return Collections.singletonList(new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    public Object getLeaderboard(Long leaderboardId) {
        try {
            String uri = gamificationAPIBaseURL + "/leaderboards/" + leaderboardId;
            return this.restTemplate.getForObject(uri, Object.class);
        }catch (Exception e){
            e.printStackTrace();
            return Collections.singletonList(new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    public List<Object> getLeaderboardResults(Long leaderboardId) {
        try {
            String uri = gamificationAPIBaseURL + "/leaderboards/" + leaderboardId + "/results";
            Object[] leaderboardResults = this.restTemplate.getForObject(uri, Object[].class);
            return Arrays.asList(leaderboardResults);
        }catch (Exception e){
            e.printStackTrace();
            return Collections.singletonList(new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
