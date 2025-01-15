package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.EvaluableActionDTO;
import edu.upc.gessi.glidegamificationengine.entity.EvaluableActionEntity;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.repository.EvaluableActionRepository;
import edu.upc.gessi.glidegamificationengine.type.ActionCategoryType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import edu.upc.gessi.glidegamificationengine.type.SourceDataToolType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvaluableActionServiceUnitTests {

    @Mock
    private EvaluableActionRepository evaluableActionRepository;

    @InjectMocks
    private EvaluableActionServiceImpl evaluableActionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEvaluableActions() {
        EvaluableActionEntity action1 = new EvaluableActionEntity("ID1", "name1", "desc1", ActionCategoryType.PlayerPerformance, PlayerType.Individual, SourceDataToolType.LearningDashboard, "url1");
        EvaluableActionEntity action2 = new EvaluableActionEntity("ID2", "name2", "desc2", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "url2");
        when(evaluableActionRepository.findAll()).thenReturn(Arrays.asList(action1, action2));

        List<EvaluableActionDTO> actions = evaluableActionService.getEvaluableActions();

        assertEquals(2, actions.size());
        verify(evaluableActionRepository, times(1)).findAll();
    }

    @Test
    void testGetEvaluableAction_ExistingId() {
        EvaluableActionEntity action = new EvaluableActionEntity("ID1", "name1", "desc1", ActionCategoryType.PlayerPerformance, PlayerType.Individual, SourceDataToolType.LearningDashboard, "url1");
        when(evaluableActionRepository.findById("ID1")).thenReturn(Optional.of(action));

        EvaluableActionDTO actionDTO = evaluableActionService.getEvaluableAction("ID1");

        assertNotNull(actionDTO);
        assertEquals("ID1", actionDTO.getId());
        verify(evaluableActionRepository, times(1)).findById("ID1");
    }

    @Test
    void testGetEvaluableAction_NonExistingId() {
        when(evaluableActionRepository.findById("NonExistingID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> evaluableActionService.getEvaluableAction("NonExistingID"));
        verify(evaluableActionRepository, times(1)).findById("NonExistingID");
    }

    @Test
    void testInitiateEvaluableActions() {
        evaluableActionService.initiateEvaluableActions();

        verify(evaluableActionRepository, atLeastOnce()).save(any(EvaluableActionEntity.class));
    }

    @Test
    void testGetEvaluableActionEntityById_ExistingId() {
        EvaluableActionEntity action = new EvaluableActionEntity("ID1", "name1", "desc1", ActionCategoryType.PlayerPerformance, PlayerType.Individual, SourceDataToolType.LearningDashboard, "url1");
        when(evaluableActionRepository.findById("ID1")).thenReturn(Optional.of(action));

        EvaluableActionEntity actionEntity = evaluableActionService.getEvaluableActionEntityById("ID1");

        assertNotNull(actionEntity);
        assertEquals("ID1", actionEntity.getId());
        verify(evaluableActionRepository, times(1)).findById("ID1");
    }

    @Test
    void testGetEvaluableActionEntityById_NonExistingId() {
        when(evaluableActionRepository.findById("NonExistingID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> evaluableActionService.getEvaluableActionEntityById("NonExistingID"));
        verify(evaluableActionRepository, times(1)).findById("NonExistingID");
    }
}
