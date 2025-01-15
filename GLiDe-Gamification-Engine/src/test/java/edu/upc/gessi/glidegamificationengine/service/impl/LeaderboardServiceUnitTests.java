package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.LeaderboardDTO;
import edu.upc.gessi.glidegamificationengine.entity.*;
import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.MissingInformationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.repository.LeaderboardRepository;
import edu.upc.gessi.glidegamificationengine.type.AnonymizationType;
import edu.upc.gessi.glidegamificationengine.type.ExtentType;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LeaderboardServiceUnitTests {
    @InjectMocks
    private LeaderboardServiceImpl leaderboardService;

    @Mock
    private LeaderboardRepository leaderboardRepository;

    @Mock
    private GameServiceImpl gameService;

    @Mock
    private AchievementServiceImpl achievementService;

    @Mock
    private GameEntity game;

    @Mock
    private AchievementEntity achievement;

    private GameKey gameKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameKey = new GameKey();
        gameKey.setSubjectAcronym("SUBJECT1");
        gameKey.setCourse(2024);
        gameKey.setPeriod(PeriodType.Quadrimester2);
    }

    @Test
    public void testCreateLeaderboard_Success() {
        String leaderboardName = "Test Leaderboard";
        Date startDate = Date.valueOf("2023-01-01");
        Date endDate = Date.valueOf("2023-12-31");
        String assessmentLevel = "Individual";
        String extent = "Subject";
        String anonymization = "None";
        boolean studentVisible = true;
        long achievementId = 1L;
        String subjectAcronym = "TEST";
        int course = 2023;
        String period = "Quadrimester2";

        when(achievementService.getAchievementEntityById(achievementId)).thenReturn(achievement);
        when(gameService.getGameEntityByKey(any())).thenReturn(game);
        when(game.hasAchievement(achievement)).thenReturn(true);

        LeaderboardEntity leaderboardEntity = new LeaderboardEntity();
        leaderboardEntity.setName(leaderboardName);
        leaderboardEntity.setStartDate(startDate);
        leaderboardEntity.setEndDate(endDate);
        leaderboardEntity.setAssessmentLevel(PlayerType.Individual);
        leaderboardEntity.setExtent(ExtentType.Subject);
        leaderboardEntity.setAnonymization(AnonymizationType.None);
        leaderboardEntity.setStudentVisible(studentVisible);
        leaderboardEntity.setAchievementEntity(achievement);
        leaderboardEntity.setGameEntity(game);

        when(leaderboardRepository.save(any(LeaderboardEntity.class))).thenReturn(leaderboardEntity);

        LeaderboardDTO result = leaderboardService.createLeaderboard(
                leaderboardName, startDate, endDate, assessmentLevel, extent, anonymization, studentVisible,
                achievementId, subjectAcronym, course, period);

        assertNotNull(result);
        assertEquals(leaderboardName, result.getName());
        verify(leaderboardRepository, times(1)).save(any(LeaderboardEntity.class));
    }

    @Test
    void testCreateLeaderboard_InvalidDates() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                leaderboardService.createLeaderboard(
                        "TestLeaderboard", Date.valueOf("2023-12-15"), Date.valueOf("2023-09-01"),
                        "Individual", "Subject", "None", true, 1L, "SUBJ", 2023, "Quadrimester2"
                )
        );
        assertEquals("Leaderboard start date cannot be posterior to leaderboard end date, please introduce different dates.", exception.getMessage());
    }

    @Test
    void testGetLeaderboard_ValidId() {
        LeaderboardEntity leaderboardEntity = new LeaderboardEntity();
        when(leaderboardRepository.findById(1L)).thenReturn(Optional.of(leaderboardEntity));

        LeaderboardDTO result = leaderboardService.getLeaderboard(1L);

        assertNotNull(result);
        verify(leaderboardRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLeaderboard_InvalidId() {
        when(leaderboardRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                leaderboardService.getLeaderboard(1L)
        );
        assertEquals("Leaderboard with id '1' not found.", exception.getMessage());
    }

    @Test
    void testGetLeaderboards_WithFilters() {
        GameEntity game = new GameEntity();
        game.setId(gameKey);
        when(gameService.getGameEntityByKey(gameKey)).thenReturn(game);

        List<LeaderboardEntity> leaderboardEntities = List.of(new LeaderboardEntity());
        when(leaderboardRepository.findByGameEntity(game)).thenReturn(leaderboardEntities);

        List<LeaderboardDTO> result = leaderboardService.getLeaderboards("SUBJECT1", 2024, "Quadrimester2");

        assertEquals(1, result.size());
        verify(leaderboardRepository, times(1)).findByGameEntity(game);
    }

    @Test
    void testGetLeaderboards_MissingFilterInfo() {
        MissingInformationException exception = assertThrows(MissingInformationException.class, () ->
                leaderboardService.getLeaderboards(null, 2023, "FALL")
        );
        assertTrue(exception.getMessage().contains("Game not fully identified"));
    }
}
