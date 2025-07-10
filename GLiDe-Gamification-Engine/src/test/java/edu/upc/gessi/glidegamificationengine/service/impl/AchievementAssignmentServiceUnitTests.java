package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.entity.AchievementAssignmentEntity;
import edu.upc.gessi.glidegamificationengine.entity.AchievementEntity;
import edu.upc.gessi.glidegamificationengine.entity.RuleEntity;
import edu.upc.gessi.glidegamificationengine.entity.SimpleRuleEntity;
import edu.upc.gessi.glidegamificationengine.repository.AchievementAssignmentRepository;
import edu.upc.gessi.glidegamificationengine.type.ConditionType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AchievementAssignmentServiceUnitTests {
    @InjectMocks
    private AchievementAssignmentServiceImpl achievementAssignmentService;

    @Mock
    private AchievementAssignmentRepository achievementAssignmentRepository;

    private AchievementEntity mockAchievementEntity;
    private RuleEntity mockRuleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockAchievementEntity = new AchievementEntity();
        mockAchievementEntity.setId(1L);

        mockRuleEntity = new SimpleRuleEntity();
        mockRuleEntity.setId(2L);
    }

    @Test
    void createAchievementAssignmentEntity_ValidInput_Success() {
        String message = "Test Message";
        Boolean onlyFirstTime = true;
        ConditionType conditionType = ConditionType.ValueEqualTo;
        List<Float> conditionParameters = List.of(10.5f);
        Integer achievementUnits = 5;
        PlayerType assessmentLevel = PlayerType.Individual;

        AchievementAssignmentEntity capturedEntity = new AchievementAssignmentEntity();
        when(achievementAssignmentRepository.save(any(AchievementAssignmentEntity.class)))
                .thenAnswer(invocation -> {
                    capturedEntity.setId(invocation.getArgument(0, AchievementAssignmentEntity.class).getId());
                    return capturedEntity;
                });

        achievementAssignmentService.createAchievementAssignmentEntity(
                mockRuleEntity, mockAchievementEntity, message, onlyFirstTime,
                conditionType, conditionParameters, achievementUnits, assessmentLevel);

        ArgumentCaptor<AchievementAssignmentEntity> captor = ArgumentCaptor.forClass(AchievementAssignmentEntity.class);
        verify(achievementAssignmentRepository, times(1)).save(captor.capture());

        AchievementAssignmentEntity savedEntity = captor.getValue();

        assertNotNull(savedEntity);
        assertEquals(mockAchievementEntity.getId(), savedEntity.getId().getAchievementId());
        assertEquals(mockRuleEntity.getId(), savedEntity.getId().getRuleId());
        assertEquals(message, savedEntity.getMessage());
        assertEquals(onlyFirstTime, savedEntity.getOnlyFirstTime());
        assertEquals(conditionType, savedEntity.getCondition());
        assertEquals(conditionParameters, savedEntity.getConditionParameters());
        assertEquals(achievementUnits, savedEntity.getAchievementUnits());
        assertEquals(assessmentLevel, savedEntity.getAssessmentLevel());
    }

    @Test
    void createAchievementAssignmentEntity_NullInput_ThrowsException() {
        mockRuleEntity = null;
        String message = "example rule";
        Boolean onlyFirstTime = true;
        ConditionType conditionType = ConditionType.ValueEqualTo;
        List<Float> conditionParameters = List.of(10.5f);
        Integer achievementUnits = 5;
        PlayerType assessmentLevel = PlayerType.Individual;

        assertThrows(NullPointerException.class, () ->
                achievementAssignmentService.createAchievementAssignmentEntity(
                        mockRuleEntity, mockAchievementEntity, message, onlyFirstTime,
                        conditionType, conditionParameters, achievementUnits, assessmentLevel));

        verify(achievementAssignmentRepository, never()).save(any(AchievementAssignmentEntity.class));
    }
}