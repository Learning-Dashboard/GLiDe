package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.DateRuleDTO;
import edu.upc.gessi.glidegamificationengine.dto.SimpleRuleDTO;
import edu.upc.gessi.glidegamificationengine.entity.*;
import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.MissingInformationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.repository.DateRuleRepository;
import edu.upc.gessi.glidegamificationengine.repository.SimpleRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RuleServiceUnitTests {

    @InjectMocks
    private RuleServiceImpl ruleService;

    @Mock
    private SimpleRuleRepository simpleRuleRepository;

    @Mock
    private DateRuleRepository dateRuleRepository;

    @Mock
    private GameServiceImpl gameService;

    @Mock
    private EvaluableActionServiceImpl evaluableActionService;

    @Mock
    private AchievementServiceImpl achievementService;

    @Mock
    private AchievementAssignmentServiceImpl achievementAssignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSimpleRuleSuccess() {
        GameEntity gameEntity = mock(GameEntity.class);
        EvaluableActionEntity actionEntity = mock(EvaluableActionEntity.class);
        AchievementEntity achievementEntity = new AchievementEntity();
        SimpleRuleEntity savedRule = new SimpleRuleEntity();
        savedRule.setName("Simple Rule 1");

        when(gameService.getGameEntityByKey(any(GameKey.class))).thenReturn(gameEntity);
        when(evaluableActionService.getEvaluableActionEntityById("action1")).thenReturn(actionEntity);
        when(achievementService.getAchievementEntityById(1L)).thenReturn(achievementEntity);
        when(simpleRuleRepository.save(any(SimpleRuleEntity.class))).thenReturn(savedRule);

        GameKey gameKey = new GameKey();
        when(gameEntity.getId()).thenReturn(gameKey);

        savedRule.setGameEntity(gameEntity);

        AchievementAssignmentEntity achievementAssignmentEntity = mock(AchievementAssignmentEntity.class);
        when(achievementAssignmentEntity.getAchievementEntity()).thenReturn(achievementEntity);
        savedRule.setAchievementAssignmentEntity(achievementAssignmentEntity);

        when(actionEntity.getId()).thenReturn("action1");
        savedRule.setEvaluableActionEntity(actionEntity);

        doNothing().when(achievementAssignmentService).createAchievementAssignmentEntity(
                any(SimpleRuleEntity.class), eq(achievementEntity), anyString(), anyBoolean(), any(), any(), anyInt(), any()
        );

        SimpleRuleDTO result = ruleService.createSimpleRule("Simple Rule 1", 3, "SUB1", 2023, "Quadrimester2", "action1", 1L, "Well done!", true, "ValueEqualTo", List.of(10f), 5, "Individual");

        assertNotNull(result);
        assertEquals("Simple Rule 1", result.getName());
        verify(simpleRuleRepository, times(1)).save(any(SimpleRuleEntity.class));
        verify(achievementAssignmentService, times(1)).createAchievementAssignmentEntity(any(), eq(achievementEntity), anyString(), anyBoolean(), any(), any(), anyInt(), any());
    }




    @Test
    void testCreateSimpleRuleInvalidRepetitions() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                ruleService.createSimpleRule("Simple Rule", 0, "SUB1", 2023, "Quadrimester2", "action1", 1L, "Good", true, "ValueGreaterThan", List.of(10f), 5, "Individual"));
        assertEquals("Simple rule repetitions cannot be less than 1, please introduce a valid number.", exception.getMessage());
    }

    @Test
    void testCreateSimpleRuleMissingParameters() {
        MissingInformationException exception = assertThrows(MissingInformationException.class, () ->
                ruleService.createSimpleRule("Simple Rule", 3, "SUB1", 2023, "Quadrimester2", "action1", 1L, "Good", true, "ValueGreaterThan", null, 5, "Individual"));
        assertTrue(exception.getMessage().contains("The given 0 achievement assignment condition parameters not the expected number"));
    }

    @Test
    void testGetSimpleRules() {
        SimpleRuleEntity rule1 = mock(SimpleRuleEntity.class);
        SimpleRuleEntity rule2 = mock(SimpleRuleEntity.class);

        when(rule1.getName()).thenReturn("Rule 1");
        when(rule2.getName()).thenReturn("Rule 2");

        GameEntity gameEntity = mock(GameEntity.class);
        GameKey gameKey = new GameKey();
        when(gameEntity.getId()).thenReturn(gameKey);

        EvaluableActionEntity actionEntity = mock(EvaluableActionEntity.class);
        when(actionEntity.getId()).thenReturn("action1");

        when(rule1.getGameEntity()).thenReturn(gameEntity);
        when(rule1.getEvaluableActionEntity()).thenReturn(actionEntity);
        when(rule2.getGameEntity()).thenReturn(gameEntity);
        when(rule2.getEvaluableActionEntity()).thenReturn(actionEntity);

        AchievementAssignmentEntity achievementAssignmentEntity = mock(AchievementAssignmentEntity.class);
        when(achievementAssignmentEntity.getAchievementEntity()).thenReturn(new AchievementEntity());
        when(rule1.getAchievementAssignmentEntity()).thenReturn(achievementAssignmentEntity);
        when(rule2.getAchievementAssignmentEntity()).thenReturn(achievementAssignmentEntity);

        when(simpleRuleRepository.findAll()).thenReturn(Arrays.asList(rule1, rule2));

        List<SimpleRuleDTO> result = ruleService.getSimpleRules(null, null, null);

        assertEquals(2, result.size());
        assertEquals("Rule 1", result.get(0).getName());
        assertEquals("Rule 2", result.get(1).getName());
        verify(simpleRuleRepository, times(1)).findAll();
    }


    @Test
    void testGetSimpleRuleNotFound() {
        when(simpleRuleRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                ruleService.getSimpleRule(1L));
        assertEquals("Simple rule with id '1' not found.", exception.getMessage());
    }

    @Test
    void testCreateDateRuleInvalidDates() {
        Date startDate = Date.valueOf("2024-12-31");
        Date endDate = Date.valueOf("2024-01-01");

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                ruleService.createDateRule("Date Rule", 3, startDate, endDate, "SUB1", 2023, "Quadrimester2", "action1", 1L, "Message", true, "ValueGreaterThan", List.of(10f), 5, "Individual"));
        assertEquals("Date rule start date cannot be posterior to the date rule end date, please introduce different dates.", exception.getMessage());
    }

    @Test
    void testCreateDateRuleSuccess() {
        GameEntity gameEntity = mock(GameEntity.class);
        EvaluableActionEntity actionEntity = mock(EvaluableActionEntity.class);
        AchievementEntity achievementEntity = new AchievementEntity();
        DateRuleEntity savedRule = new DateRuleEntity();
        savedRule.setName("Date Rule 1");

        when(gameService.getGameEntityByKey(any(GameKey.class))).thenReturn(gameEntity);
        when(evaluableActionService.getEvaluableActionEntityById("action1")).thenReturn(actionEntity);
        when(achievementService.getAchievementEntityById(1L)).thenReturn(achievementEntity);
        when(dateRuleRepository.save(any(DateRuleEntity.class))).thenReturn(savedRule);

        GameKey gameKey = new GameKey();
        when(gameEntity.getId()).thenReturn(gameKey);

        savedRule.setGameEntity(gameEntity);

        AchievementAssignmentEntity achievementAssignmentEntity = mock(AchievementAssignmentEntity.class);
        when(achievementAssignmentEntity.getAchievementEntity()).thenReturn(achievementEntity);
        savedRule.setAchievementAssignmentEntity(achievementAssignmentEntity);

        when(actionEntity.getId()).thenReturn("action1");
        savedRule.setEvaluableActionEntity(actionEntity);

        doNothing().when(achievementAssignmentService).createAchievementAssignmentEntity(
                any(DateRuleEntity.class), eq(achievementEntity), anyString(), anyBoolean(), any(), any(), anyInt(), any()
        );

        DateRuleDTO result = ruleService.createDateRule("Date Rule 1", 3, Date.valueOf("2024-01-01"), Date.valueOf("2024-12-31"), "SUB1", 2023, "Quadrimester2", "action1", 1L, "Well done!", true, "ValueGreaterThan", List.of(10f), 5, "Individual");

        assertNotNull(result);
        assertEquals("Date Rule 1", result.getName());
        verify(dateRuleRepository, times(1)).save(any(DateRuleEntity.class));
        verify(achievementAssignmentService, times(1)).createAchievementAssignmentEntity(any(), eq(achievementEntity), anyString(), anyBoolean(), any(), any(), anyInt(), any());
    }

}

