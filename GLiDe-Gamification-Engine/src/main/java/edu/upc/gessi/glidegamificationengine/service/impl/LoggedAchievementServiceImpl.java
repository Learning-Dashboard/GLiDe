package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.entity.AchievementAssignmentEntity;
import edu.upc.gessi.glidegamificationengine.entity.LoggedAchievementEntity;
import edu.upc.gessi.glidegamificationengine.entity.PlayerEntity;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.repository.LoggedAchievementRepository;
import edu.upc.gessi.glidegamificationengine.service.LoggedAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class LoggedAchievementServiceImpl implements LoggedAchievementService {

    @Autowired
    private LoggedAchievementRepository loggedAchievementRepository;

    /* Methods callable from Service Layer */

    protected LoggedAchievementEntity createLoggedAchievementEntity(Date loggedAchievementDate, AchievementAssignmentEntity achievementAssignmentEntity, PlayerEntity playerEntity) {
        LoggedAchievementEntity loggedAchievementEntity = new LoggedAchievementEntity(loggedAchievementDate, achievementAssignmentEntity, playerEntity);
        achievementAssignmentEntity.addLoggedAchievementEntity(loggedAchievementEntity);
        playerEntity.addLoggedAchievementEntity(loggedAchievementEntity);
        return loggedAchievementRepository.save(loggedAchievementEntity);
    }

    protected LoggedAchievementEntity getLoggedAchievementEntityById(Long loggedAchievementId) {
        return loggedAchievementRepository.findById(loggedAchievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Logged achievement with id '" + loggedAchievementId + "' not found."));
    }


    /* Methods callable from Controller Layer */

}
