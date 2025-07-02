package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.entity.IndividualPlayerEntity;
import edu.upc.gessi.glidebackend.repository.IndividualPlayerRepository;
import edu.upc.gessi.glidebackend.service.GamificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.Optional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GamificationServiceImpl implements GamificationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private IndividualPlayerRepository individualPlayerRepository;

    @Value("${gamification.api.base-url}")
    private String gamificationAPIBaseURL;

    @Override
    public Object getIndividualPlayer(String individualPlayerPlayername) {
        try {
            String uri = gamificationAPIBaseURL + "/players/individuals/" + individualPlayerPlayername;
            Map<String, Object> playerData = restTemplate.getForObject(uri, Map.class);
            Optional<IndividualPlayerEntity> playerEntityOpt = individualPlayerRepository.findById(individualPlayerPlayername);
            System.out.println("individualPlayerEntityOpt: " + playerEntityOpt);
            if (playerEntityOpt.isPresent()) {
                String nickname = playerEntityOpt.get().getStudentUserEntity().getNickname();
                System.out.println("Nickname: " + nickname);
                playerData.put("nickname", nickname);
            }
            return playerData;
        }catch (Exception e){
            e.printStackTrace();
            return Collections.singletonList(new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Override
    public Object getTeamPlayer(String teamPlayerPlayername) {
        try {
            String uri = gamificationAPIBaseURL + "/players/teams/" + teamPlayerPlayername;
            return this.restTemplate.getForObject(uri, Object.class);
        }catch (Exception e){
            e.printStackTrace();
            return Collections.singletonList(new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Override
    public List<Object> getPlayerAchievements(String teamPlayerPlayername, String attained, String category) {
        try {
            String uri = gamificationAPIBaseURL + "/players/" + teamPlayerPlayername + "/achievements?attained=" + attained + "&category=" + category;
            Object[] achievements = this.restTemplate.getForObject(uri, Object[].class);
            return Arrays.asList(achievements);
        }catch (Exception e){
            e.printStackTrace();
            return Collections.singletonList(new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Override
    public List<Object> getEvaluableActions() {
        try {
            String uri = gamificationAPIBaseURL + "/evaluableActions";
            Object[] evaluableActions = this.restTemplate.getForObject(uri, Object[].class);
            return Arrays.asList(evaluableActions);
        } catch (Exception e) {
            return Collections.singletonList(new ResponseEntity<>("Error! Please try again", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
