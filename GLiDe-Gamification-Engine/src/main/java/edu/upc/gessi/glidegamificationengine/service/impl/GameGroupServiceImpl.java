package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.GameGroupDTO;
import edu.upc.gessi.glidegamificationengine.entity.GameEntity;
import edu.upc.gessi.glidegamificationengine.entity.GameGroupEntity;
import edu.upc.gessi.glidegamificationengine.entity.key.GameGroupKey;
import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.mapper.GameGroupMapper;
import edu.upc.gessi.glidegamificationengine.repository.GameGroupRepository;
import edu.upc.gessi.glidegamificationengine.repository.GameRepository;
import edu.upc.gessi.glidegamificationengine.service.GameGroupService;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameGroupServiceImpl implements GameGroupService {

    @Autowired
    private GameGroupRepository gameGroupRepository;
    @Autowired
    private GameRepository gameRepository;

    @Override
    public GameGroupDTO createGroup(Integer gameCourse, String gamePeriod, String gameSubjectAcronym, Integer group){
        if (gameCourse == null || gamePeriod.isBlank() || gameSubjectAcronym.isBlank() || group == null)
            throw new ConstraintViolationException("Parameter is null or empty.");
        GameGroupKey gameGroupKey = new GameGroupKey();
        gameGroupKey.setGameCourse(gameCourse);
        gameGroupKey.setGamePeriod(PeriodType.fromString(gamePeriod));
        gameGroupKey.setGameSubjectAcronym(gameSubjectAcronym);
        gameGroupKey.setGroup(group);
        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setCourse(gameCourse);
        gameKey.setPeriod(PeriodType.fromString(gamePeriod));

        GameEntity gameEntity = gameRepository.findById(gameKey)
                .orElseThrow(() -> new ResourceNotFoundException("Game with acronym: " + gameSubjectAcronym + ", course: " + gameCourse + ", period: " + gamePeriod + " not found."));
        GameGroupEntity gameGroupEntity = new GameGroupEntity();
        gameGroupEntity.setId(gameGroupKey);
        gameGroupEntity.setGameEntity(gameEntity);

        if (gameGroupRepository.existsById(gameGroupKey)) {
            throw new ConstraintViolationException("Repeated game group.");
        }

        GameGroupEntity savedGameGroup = gameGroupRepository.save(gameGroupEntity);
        return GameGroupMapper.mapToGameGroupDTO(savedGameGroup);

    }
}
