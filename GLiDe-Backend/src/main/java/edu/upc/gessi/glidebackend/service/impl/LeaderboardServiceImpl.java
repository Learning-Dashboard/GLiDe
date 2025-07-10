package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.repository.IndividualPlayerRepository;
import edu.upc.gessi.glidebackend.entity.IndividualPlayerEntity;
import edu.upc.gessi.glidebackend.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private IndividualPlayerRepository individualPlayerRepository;

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
            Object[] leaderboardResponse = this.restTemplate.getForObject(uri, Object[].class);

            List<Map<String, Object>> enrichedResults = new ArrayList<>();

            for (Object entry : leaderboardResponse) {
                if (entry instanceof Map<?, ?> rawEntry) {
                    Map<String, Object> result = new HashMap<>();
                    for (Map.Entry<?, ?> e : rawEntry.entrySet()) {
                        if (e.getKey() instanceof String) {
                            result.put((String) e.getKey(), e.getValue());
                        }
                    }

                    Object resultsListObj = result.get("leaderboardResults");
                    if (resultsListObj instanceof List<?> leaderboardEntries) {
                        for (Object playerObj : leaderboardEntries) {
                            if (playerObj instanceof Map<?, ?> playerEntry) {
                                String playername = (String) playerEntry.get("playername");
                                if (playername != null) {
                                    Optional<IndividualPlayerEntity> playerOpt = individualPlayerRepository.findById(playername);
                                    playerOpt.ifPresent(player -> {
                                        String nickname = player.getStudentUserEntity().getNickname();
                                        if (nickname != null) {
                                            ((Map<String, Object>) playerEntry).put("nickname", nickname);
                                        }
                                    });
                                }
                            }
                        }
                    }

                    enrichedResults.add(result);
                }
            }

            return new ArrayList<>(enrichedResults);

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonList(
                new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }
}
