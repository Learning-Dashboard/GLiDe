package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.GameDTO;
import edu.upc.gessi.glidegamificationengine.entity.*;
import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.repository.GameRepository;
import edu.upc.gessi.glidegamificationengine.repository.SubjectRepository;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceUnitTests {

    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private SubjectRepository subjectRepository;

    private GameKey gameKey;
    private GameEntity gameEntity;
    private SubjectEntity subjectEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        gameKey = new GameKey();
        gameKey.setSubjectAcronym("SUBJECT1");
        gameKey.setCourse(2024);
        gameKey.setPeriod(PeriodType.Quadrimester2);

        subjectEntity = new SubjectEntity();
        subjectEntity.setAcronym("SUBJECT1");

        gameEntity = new GameEntity();
        gameEntity.setId(gameKey);
        gameEntity.setStartDate(Date.valueOf("2024-02-01"));
        gameEntity.setEndDate(Date.valueOf("2024-06-01"));
        gameEntity.setSubjectEntity(subjectEntity);
    }

    @Test
    void testCreateGame_ValidInput_Success() {
        when(subjectRepository.findById("SUBJECT1")).thenReturn(Optional.of(subjectEntity));
        when(gameRepository.existsById(gameKey)).thenReturn(false);
        when(gameRepository.save(any(GameEntity.class))).thenReturn(gameEntity);

        GameDTO result = gameService.createGame(
                "SUBJECT1", 2024, "Quadrimester2",
                Date.valueOf("2024-02-01"), Date.valueOf("2024-06-01"),
                1.0f, 2.0f, 3.0f);

        assertNotNull(result);
        assertEquals("SUBJECT1", result.getSubjectAcronym());
        assertEquals(2024, result.getCourse());
        assertEquals("Quadrimester2", result.getPeriod().toString());

        verify(gameRepository).save(any(GameEntity.class));
    }

    @Test
    void testCreateGame_GameAlreadyExists_ThrowsException() {
        when(subjectRepository.findById("SUBJECT1")).thenReturn(Optional.of(subjectEntity));
        when(gameRepository.existsById(gameKey)).thenReturn(true);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            gameService.createGame(
                    "SUBJECT1", 2024, "Quadrimester2",
                    Date.valueOf("2024-02-01"), Date.valueOf("2024-06-01"),
                    1.0f, 2.0f, 3.0f);
        });

        assertEquals("This game already exists.", exception.getMessage());
        verify(gameRepository, never()).save(any(GameEntity.class));
    }

    @Test
    void testCreateGame_SubjectNotFound_ThrowsException() {
        when(subjectRepository.findById("SUBJECT1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            gameService.createGame(
                    "SUBJECT1", 2024, "Quadrimester2",
                    Date.valueOf("2024-02-01"), Date.valueOf("2024-06-01"),
                    1.0f, 2.0f, 3.0f);
        });

        assertEquals("Subject SUBJECT1 not found.", exception.getMessage());
        verify(gameRepository, never()).save(any(GameEntity.class));
    }

    @Test
    void testGetGames_BySubject_Success() {
        when(gameRepository.findByIdSubjectAcronym("SUBJECT1"))
                .thenReturn(Collections.singletonList(gameEntity));

        List<GameDTO> result = gameService.getGames("SUBJECT1", null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SUBJECT1", result.getFirst().getSubjectAcronym());
        verify(gameRepository).findByIdSubjectAcronym("SUBJECT1");
    }

    @Test
    void testEvaluateGame_GameNotFound_ThrowsException() {
        when(gameRepository.findById(gameKey)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            gameService.evaluateGame("SUBJECT1", 2024, "Quadrimester2", LocalDate.now());
        });

        assertEquals("Game with subject acronym 'SUBJECT1', course '2024' and period 'Quadrimester2' not found.", exception.getMessage());
        verify(gameRepository).findById(gameKey);
    }

    @Test
    void testGetGames_AllGames_Success() {
        when(gameRepository.findAll()).thenReturn(Collections.singletonList(gameEntity));

        List<GameDTO> result = gameService.getGames(null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SUBJECT1", result.getFirst().getSubjectAcronym());
        verify(gameRepository).findAll();
    }
}

