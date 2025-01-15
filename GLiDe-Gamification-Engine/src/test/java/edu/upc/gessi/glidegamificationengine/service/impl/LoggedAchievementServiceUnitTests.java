package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.entity.AchievementAssignmentEntity;
import edu.upc.gessi.glidegamificationengine.entity.LoggedAchievementEntity;
import edu.upc.gessi.glidegamificationengine.entity.PlayerEntity;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.repository.LoggedAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoggedAchievementServiceUnitTests {
    @Mock
    private LoggedAchievementRepository loggedAchievementRepository;

    @InjectMocks
    private LoggedAchievementServiceImpl loggedAchievementService;

    @Mock
    private AchievementAssignmentEntity achievementAssignment;

    @Mock
    private PlayerEntity player;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLoggedAchievementEntity_Success() {
        Date loggedDate = Date.valueOf("2023-12-10");
        ArgumentCaptor<LoggedAchievementEntity> captor = ArgumentCaptor.forClass(LoggedAchievementEntity.class);

        when(loggedAchievementRepository.save(any(LoggedAchievementEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LoggedAchievementEntity result = loggedAchievementService.createLoggedAchievementEntity(loggedDate, achievementAssignment, player);

        assertNotNull(result);
        assertEquals(loggedDate, result.getDate());
        assertEquals(achievementAssignment, result.getAchievementAssignmentEntity());
        assertEquals(player, result.getPlayerEntity());

        verify(loggedAchievementRepository, times(1)).save(captor.capture());
        verify(achievementAssignment, times(1)).addLoggedAchievementEntity(captor.getValue());
        verify(player, times(1)).addLoggedAchievementEntity(captor.getValue());

        LoggedAchievementEntity captured = captor.getValue();
        assertEquals(loggedDate, captured.getDate());
        assertEquals(achievementAssignment, captured.getAchievementAssignmentEntity());
        assertEquals(player, captured.getPlayerEntity());
    }

    @Test
    public void testGetLoggedAchievementEntityById_Success() {
        Long loggedAchievementId = 1L;
        Date loggedDate = Date.valueOf("2023-12-10");
        LoggedAchievementEntity loggedAchievement = new LoggedAchievementEntity(loggedDate, achievementAssignment, player);

        when(loggedAchievementRepository.findById(loggedAchievementId)).thenReturn(Optional.of(loggedAchievement));

        LoggedAchievementEntity result = loggedAchievementService.getLoggedAchievementEntityById(loggedAchievementId);

        assertNotNull(result);
        assertEquals(loggedAchievement, result);
        verify(loggedAchievementRepository, times(1)).findById(loggedAchievementId);
    }

    @Test
    public void testGetLoggedAchievementEntityById_ThrowsResourceNotFoundException() {
        Long loggedAchievementId = 1L;
        when(loggedAchievementRepository.findById(loggedAchievementId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                loggedAchievementService.getLoggedAchievementEntityById(loggedAchievementId));

        assertEquals("Logged achievement with id '1' not found.", exception.getMessage());
        verify(loggedAchievementRepository, times(1)).findById(loggedAchievementId);
    }
}
