package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.entity.AchievementAssignmentEntity;
import edu.upc.gessi.glidegamificationengine.entity.AchievementEntity;
import edu.upc.gessi.glidegamificationengine.entity.RuleEntity;
import edu.upc.gessi.glidegamificationengine.entity.key.AchievementAssignmentKey;
import edu.upc.gessi.glidegamificationengine.repository.AchievementAssignmentRepository;
import edu.upc.gessi.glidegamificationengine.service.AchievementAssignmentService;
import edu.upc.gessi.glidegamificationengine.type.ConditionType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementAssignmentServiceImpl implements AchievementAssignmentService {

    @Autowired
    private AchievementAssignmentRepository achievementAssignmentRepository;

    /* Methods callable from Service Layer */

    protected void createAchievementAssignmentEntity(RuleEntity ruleEntity, AchievementEntity achievementEntity, String achievementAssignmentMessage, Boolean achievementAssignmentOnlyFirstTime, ConditionType achievementAssignmentCondition, List<Float> achievementAssignmentConditionParameters, Integer achievementAssignmentUnits, PlayerType achievementAssignmentAssessmentLevel) {
        AchievementAssignmentKey achievementAssignmentKey = new AchievementAssignmentKey();
        achievementAssignmentKey.setAchievementId(achievementEntity.getId());
        achievementAssignmentKey.setRuleId(ruleEntity.getId());

        AchievementAssignmentEntity achievementAssignmentEntity = new AchievementAssignmentEntity();
        achievementAssignmentEntity.setId(achievementAssignmentKey);
        achievementAssignmentEntity.setMessage(achievementAssignmentMessage);
        achievementAssignmentEntity.setOnlyFirstTime(achievementAssignmentOnlyFirstTime);
        achievementAssignmentEntity.setCondition(achievementAssignmentCondition);
        achievementAssignmentEntity.setConditionParameters(achievementAssignmentConditionParameters);
        achievementAssignmentEntity.setAchievementUnits(achievementAssignmentUnits);
        achievementAssignmentEntity.setAssessmentLevel(achievementAssignmentAssessmentLevel);
        achievementAssignmentEntity.setAchievementEntity(achievementEntity);
        achievementAssignmentEntity.setRuleEntity(ruleEntity);

        AchievementAssignmentEntity savedAchievementAssignmentEntity = achievementAssignmentRepository.save(achievementAssignmentEntity);

        achievementEntity.addAchievementAssignmentEntity(savedAchievementAssignmentEntity);
        ruleEntity.setAchievementAssignmentEntity(savedAchievementAssignmentEntity);
    }

    protected void updateAchievementAssignmentEntity(RuleEntity ruleEntity, String achievementAssignmentMessage, Boolean achievementAssignmentOnlyFirstTime, ConditionType achievementAssignmentCondition, List<Float> achievementAssignmentConditionParameters, Integer achievementAssignmentUnits, PlayerType achievementAssignmentAssessmentLevel){
        Optional<AchievementAssignmentEntity> optionalAchievementAssignmentEntity = achievementAssignmentRepository.findById(ruleEntity.getAchievementAssignmentEntity().getId());
        if(optionalAchievementAssignmentEntity.isPresent()){
            AchievementAssignmentEntity achievementAssignmentEntity = optionalAchievementAssignmentEntity.get();

            achievementAssignmentEntity.setMessage(achievementAssignmentMessage);
            achievementAssignmentEntity.setOnlyFirstTime(achievementAssignmentOnlyFirstTime);
            achievementAssignmentEntity.setCondition(achievementAssignmentCondition);
            achievementAssignmentEntity.setConditionParameters(achievementAssignmentConditionParameters);
            achievementAssignmentEntity.setAchievementUnits(achievementAssignmentUnits);
            achievementAssignmentEntity.setAssessmentLevel(achievementAssignmentAssessmentLevel);

            achievementAssignmentRepository.save(achievementAssignmentEntity);

        }
        else {
            throw new EntityNotFoundException("AchievementAssignmentEntity not found for the provided key.");
        }
    }

    /* Methods callable from Controller Layer */

}
