package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.entity.*;
import edu.upc.gessi.glidegamificationengine.entity.key.LoggedActionKey;
import edu.upc.gessi.glidegamificationengine.repository.LoggedActionRepository;
import edu.upc.gessi.glidegamificationengine.repository.PlayerPerformanceLoggedActionRepository;
import edu.upc.gessi.glidegamificationengine.service.LoggedActionService;
import edu.upc.gessi.glidegamificationengine.type.ActionCategoryType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import edu.upc.gessi.glidegamificationengine.type.SourceDataToolType;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class LoggedActionServiceImpl implements LoggedActionService {

    @Autowired
    private LoggedActionRepository loggedActionRepository;

    @Autowired
    private PlayerPerformanceLoggedActionRepository playerPerformanceLoggedActionRepository;

    @Autowired
    private WebClient webClient;

    /* Properties defined in application.properties file */

    @Value("${learningdashboard.api.base-url}")
    private String learningdashboardAPIBaseURL;

    @Value("${learningdashboard.api.key}")
    private String learningdashboardAPIKey;


    /* Private methods */

    private LoggedActionEntity createLoggedActionEntityByKeyEntities(LoggedActionKey loggedActionKey, EvaluableActionEntity evaluableActionEntity, PlayerEntity playerEntity, Timestamp timestamp) {
        String sourceDataAPIURL = "";
        String sourceDataAPIKey = "";

        if (evaluableActionEntity.getSourceDataTool().equals(SourceDataToolType.LearningDashboard)) {
            sourceDataAPIURL = learningdashboardAPIBaseURL + evaluableActionEntity.getSourceDataAPIEndpoint();
            if (evaluableActionEntity.getAssessmentLevel().equals(PlayerType.Team)) {
                TeamPlayerEntity teamPlayerEntity = (TeamPlayerEntity) playerEntity;
                sourceDataAPIURL = sourceDataAPIURL.replaceFirst("\\*", evaluableActionEntity.getName());
                sourceDataAPIURL = sourceDataAPIURL.replaceFirst("\\*", teamPlayerEntity.getProjectEntity().getLearningdashboardIdentifier());
            }
            else {
                IndividualPlayerEntity individualPlayerEntity = (IndividualPlayerEntity) playerEntity;
                if (evaluableActionEntity.getName().contains("tasks")) sourceDataAPIURL = sourceDataAPIURL.replaceFirst("\\*", evaluableActionEntity.getName() + "_" + individualPlayerEntity.getStudentUserEntity().getTaigaUsername());
                else sourceDataAPIURL = sourceDataAPIURL.replaceFirst("\\*", evaluableActionEntity.getName() + "_" + individualPlayerEntity.getStudentUserEntity().getGithubUsername());
                sourceDataAPIURL = sourceDataAPIURL.replaceFirst("\\*", individualPlayerEntity.getTeamPlayerEntity().getProjectEntity().getLearningdashboardIdentifier());
            }
            sourceDataAPIKey = learningdashboardAPIKey;
        }

        String jsonString = webClient.get()
                .uri(sourceDataAPIURL)
                .header("password", sourceDataAPIKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONObject jsonObject = new JSONObject(jsonString);

        LoggedActionEntity loggedActionEntity = null;

        if (evaluableActionEntity.getCategory().equals(ActionCategoryType.PlayerPerformance)) {
            Float value = null;
            try {
                value = jsonObject.getFloat("value");
            } catch (JSONException e){
                JSONObject nestedJsonObject = jsonObject.getJSONObject("value");
                value = nestedJsonObject.getFloat("first");
            }

            PlayerPerformanceLoggedActionEntity playerPerformanceLoggedActionEntity = new PlayerPerformanceLoggedActionEntity(loggedActionKey, evaluableActionEntity, playerEntity, value);
            loggedActionEntity = playerPerformanceLoggedActionRepository.save(playerPerformanceLoggedActionEntity);
        }

        return loggedActionEntity;
    }


    /* Methods callable from Service Layer */

    protected LoggedActionEntity getOrCreateLoggedActionEntityByKeyEntities(EvaluableActionEntity evaluableActionEntity, PlayerEntity playerEntity, Timestamp timestamp) {
        LoggedActionKey loggedActionKey = new LoggedActionKey();
        loggedActionKey.setEvaluableActionId(evaluableActionEntity.getId());
        loggedActionKey.setPlayerPlayername(playerEntity.getPlayername());
        loggedActionKey.setTimestamp(timestamp);

        Optional<LoggedActionEntity> loggedActionEntity = loggedActionRepository.findById(loggedActionKey);
        if (loggedActionEntity.isPresent()) {
            return loggedActionEntity.get();
        } else {
            return createLoggedActionEntityByKeyEntities(loggedActionKey, evaluableActionEntity, playerEntity, timestamp);
        }
    }


    /* Methods callable from Controller Layer */

}
