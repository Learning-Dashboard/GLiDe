package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.GameGroupDTO;
import edu.upc.gessi.glidegamificationengine.entity.GameEntity;
import edu.upc.gessi.glidegamificationengine.entity.GameGroupEntity;
import edu.upc.gessi.glidegamificationengine.entity.key.GameGroupKey;
import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.repository.GameGroupRepository;
import edu.upc.gessi.glidegamificationengine.repository.GameRepository;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameGroupServiceUnitTests {

    @Mock
    private GameGroupRepository gameGroupRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameGroupServiceImpl gameGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGroup_Success() {
        Integer gameCourse = 2023;
        String gamePeriod = "Quadrimester2";
        String gameSubjectAcronym = "CS101";
        Integer group = 1;

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setPeriod(PeriodType.Quadrimester2);
        gameKey.setCourse(gameCourse);

        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(gameKey);

        GameGroupKey gameGroupKey = new GameGroupKey();
        gameGroupKey.setGamePeriod(PeriodType.Quadrimester2);
        gameGroupKey.setGameCourse(gameCourse);
        gameGroupKey.setGameSubjectAcronym(gameSubjectAcronym);
        gameGroupKey.setGroup(group);

        GameGroupEntity gameGroupEntity = new GameGroupEntity();
        gameGroupEntity.setId(gameGroupKey);
        gameGroupEntity.setGameEntity(gameEntity);

        when(gameRepository.findById(gameKey)).thenReturn(Optional.of(gameEntity));
        when(gameGroupRepository.existsById(gameGroupKey)).thenReturn(false);
        when(gameGroupRepository.save(any(GameGroupEntity.class))).thenReturn(gameGroupEntity);

        GameGroupDTO result = gameGroupService.createGroup(gameCourse, gamePeriod, gameSubjectAcronym, group);

        assertNotNull(result);
        assertEquals(gameGroupKey, result.getId());
        assertEquals(gameGroupKey.getGameSubjectAcronym(), result.getId().getGameSubjectAcronym());
        verify(gameRepository, times(1)).findById(gameKey);
        verify(gameGroupRepository, times(1)).save(any(GameGroupEntity.class));
    }

    @Test
    void createGroup_GameNotFound() {
        Integer gameCourse = 2023;
        String gamePeriod = "Quadrimester2";
        String gameSubjectAcronym = "CS101";
        Integer group = 1;

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setPeriod(PeriodType.Quadrimester2);
        gameKey.setCourse(gameCourse);

        when(gameRepository.findById(gameKey)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                gameGroupService.createGroup(gameCourse, gamePeriod, gameSubjectAcronym, group));
        assertEquals("Game with acronym: CS101, course: 2023, period: Quadrimester2 not found.", exception.getMessage());
        verify(gameRepository, times(1)).findById(gameKey);
        verify(gameGroupRepository, never()).save(any(GameGroupEntity.class));
    }

    @Test
    void createGroup_RepeatedGroup() {
        Integer gameCourse = 2023;
        String gamePeriod = "Quadrimester2";
        String gameSubjectAcronym = "CS101";
        Integer group = 1;

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setPeriod(PeriodType.Quadrimester2);
        gameKey.setCourse(gameCourse);
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(gameKey);

        GameGroupKey gameGroupKey = new GameGroupKey();
        gameGroupKey.setGamePeriod(PeriodType.Quadrimester2);
        gameGroupKey.setGameCourse(gameCourse);
        gameGroupKey.setGameSubjectAcronym(gameSubjectAcronym);
        gameGroupKey.setGroup(group);

        when(gameRepository.findById(gameKey)).thenReturn(Optional.of(gameEntity));
        when(gameGroupRepository.existsById(gameGroupKey)).thenReturn(true);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                gameGroupService.createGroup(gameCourse, gamePeriod, gameSubjectAcronym, group));
        assertEquals("Repeated game group.", exception.getMessage());
        verify(gameRepository, times(1)).findById(gameKey);
        verify(gameGroupRepository, never()).save(any(GameGroupEntity.class));
    }

    @Test
    void createGroup_InvalidParameters() {
        Integer gameCourse = null;
        String gamePeriod = "";
        String gameSubjectAcronym = "";
        Integer group = null;

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                gameGroupService.createGroup(gameCourse, gamePeriod, gameSubjectAcronym, group));
        assertEquals("Parameter is null or empty.", exception.getMessage());
        verify(gameRepository, never()).findById(any(GameKey.class));
        verify(gameGroupRepository, never()).save(any(GameGroupEntity.class));
    }
}
