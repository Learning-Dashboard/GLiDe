package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.dto.*;
import edu.upc.gessi.glidebackend.entity.*;
import edu.upc.gessi.glidebackend.exception.ResourceNotFoundException;
import edu.upc.gessi.glidebackend.mapper.PlayerMapper;
import edu.upc.gessi.glidebackend.mapper.PlayerMonitoringMapper;
import edu.upc.gessi.glidebackend.mapper.StudentUserMapper;
import edu.upc.gessi.glidebackend.repository.IndividualPlayerRepository;
import edu.upc.gessi.glidebackend.repository.PlayerMonitoringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceUnitTests {
    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private IndividualPlayerRepository individualPlayerRepository;

    @Mock
    private PlayerMonitoringRepository playerMonitoringRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIndividualPlayer_success() {
        String playername = "testPlayer";
        IndividualPlayerEntity mockEntity = new IndividualPlayerEntity();
        mockEntity.setPlayername(playername);

        when(individualPlayerRepository.findById(playername)).thenReturn(Optional.of(mockEntity));
        try (MockedStatic<PlayerMapper> mockedMapper = mockStatic(PlayerMapper.class)) {
            mockedMapper.when(() -> PlayerMapper.mapToIndividualPlayerDto(mockEntity))
                    .thenReturn(new IndividualPlayerDto());

            IndividualPlayerDto result = playerService.getIndividualPlayer(playername);

            assertNotNull(result);
            verify(individualPlayerRepository, times(1)).findById(playername);
        }
    }

    @Test
    void testGetIndividualPlayer_NotFound() {
        String individualPlayerPlayername = "nonexistent";

        when(individualPlayerRepository.findById(individualPlayerPlayername)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getIndividualPlayer(individualPlayerPlayername));
        verify(individualPlayerRepository, times(1)).findById(individualPlayerPlayername);
    }

    @Test
    void testGetAllIndividualPlayers_success() {
        String playername = "testPlayer";
        IndividualPlayerEntity mockEntity = new IndividualPlayerEntity();

        List<IndividualPlayerEntity> mockEntities = List.of(mockEntity);
        when(individualPlayerRepository.findAll()).thenReturn(mockEntities);

        IndividualPlayerDto individualPlayerDto = new IndividualPlayerDto();
        individualPlayerDto.setPlayername(playername);
        try (MockedStatic<PlayerMapper> mockedMapper = mockStatic(PlayerMapper.class)) {
            mockedMapper.when(() -> PlayerMapper.mapToIndividualPlayerDto(mockEntity))
                    .thenReturn(individualPlayerDto);

            List<IndividualPlayerDto> result = playerService.getAllIndividualPlayers();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(individualPlayerDto, result.getFirst());
            verify(individualPlayerRepository, times(1)).findAll();
        }
    }

    @Test
    void testGetStudentUser_success() {
        String playername = "testPlayer";
        IndividualPlayerEntity mockEntity = new IndividualPlayerEntity();
        StudentUserEntity mockStudentUserEntity = new StudentUserEntity();
        mockEntity.setStudentUserEntity(mockStudentUserEntity);
        when(individualPlayerRepository.findById(playername)).thenReturn(Optional.of(mockEntity));
        StudentUserDto mockDto = new StudentUserDto();
        try (MockedStatic<StudentUserMapper> mockedMapper = mockStatic(StudentUserMapper.class)) {
            mockedMapper.when(() -> StudentUserMapper.mapToStudentUserDto(mockStudentUserEntity))
                    .thenReturn(mockDto);

            StudentUserDto result = playerService.getStudentUser(playername);

            assertNotNull(result);
            assertEquals(mockDto, result);
            verify(individualPlayerRepository, times(1)).findById(playername);
        }
    }

    @Test
    void testGetPlayerMonitoring_success() {
        String playername = "testPlayer";
        IndividualPlayerEntity mockEntity = new IndividualPlayerEntity();
        PlayerMonitoringEntity mockMonitoringEntity = new PlayerMonitoringEntity();
        when(individualPlayerRepository.findById(playername)).thenReturn(Optional.of(mockEntity));
        when(playerMonitoringRepository.findByIndividualPlayerEntity(mockEntity)).thenReturn(mockMonitoringEntity);
        PlayerMonitoringDto mockDto = new PlayerMonitoringDto();
        try (MockedStatic<PlayerMonitoringMapper> mockedMapper = mockStatic(PlayerMonitoringMapper.class)) {
            mockedMapper.when(() -> PlayerMonitoringMapper.mapToPlayerMonitoringDto(mockMonitoringEntity))
                    .thenReturn(mockDto);

            PlayerMonitoringDto result = playerService.getPlayerMonitoring(playername);

            assertNotNull(result);
            assertEquals(mockDto, result);
            verify(individualPlayerRepository, times(1)).findById(playername);
            verify(playerMonitoringRepository, times(1)).findByIndividualPlayerEntity(mockEntity);
        }
    }

    @Test
    void testSetPlayerMonitoring_success() {
        String playername = "testPlayer";
        String selectedMetrics = "metrics1";
        String selectedHistoryMetrics = "metrics2";
        String selectedBarMetrics = "metrics3";
        IndividualPlayerEntity mockEntity = new IndividualPlayerEntity();
        PlayerMonitoringEntity mockMonitoringEntity = new PlayerMonitoringEntity();
        when(individualPlayerRepository.findById(playername)).thenReturn(Optional.of(mockEntity));
        when(playerMonitoringRepository.findByIndividualPlayerEntity(mockEntity)).thenReturn(mockMonitoringEntity);
        PlayerMonitoringDto mockDto = new PlayerMonitoringDto();
        try (MockedStatic<PlayerMonitoringMapper> mockedMapper = mockStatic(PlayerMonitoringMapper.class)) {
            mockedMapper.when(() -> PlayerMonitoringMapper.mapToPlayerMonitoringDto(mockMonitoringEntity))
                    .thenReturn(mockDto);

            PlayerMonitoringDto result = playerService.setPlayerMonitoring(playername, selectedMetrics, selectedHistoryMetrics, selectedBarMetrics);

            assertNotNull(result);
            assertEquals(mockDto, result);
            assertEquals(selectedMetrics, mockMonitoringEntity.getSelectedMetrics());
            assertEquals(selectedHistoryMetrics, mockMonitoringEntity.getSelectedHistoryMetrics());
            assertEquals(selectedBarMetrics, mockMonitoringEntity.getSelectedBarMetrics());
            verify(playerMonitoringRepository, times(1)).save(mockMonitoringEntity);
        }
    }

    @Test
    void testSetPlayerMonitoringDates_success() {
        String playername = "testPlayer";
        String startDate = "2023-01-01";
        String endDate = "2023-12-31";
        IndividualPlayerEntity mockEntity = new IndividualPlayerEntity();
        PlayerMonitoringEntity mockMonitoringEntity = new PlayerMonitoringEntity();
        when(individualPlayerRepository.findById(playername)).thenReturn(Optional.of(mockEntity));
        when(playerMonitoringRepository.findByIndividualPlayerEntity(mockEntity)).thenReturn(mockMonitoringEntity);
        PlayerMonitoringDto mockDto = new PlayerMonitoringDto();

        try (MockedStatic<PlayerMonitoringMapper> mockedMapper = mockStatic(PlayerMonitoringMapper.class)) {
            mockedMapper.when(() -> PlayerMonitoringMapper.mapToPlayerMonitoringDto(mockMonitoringEntity))
                    .thenReturn(mockDto);
            PlayerMonitoringDto result = playerService.setPlayerMonitoringDates(playername, startDate, endDate);

            assertNotNull(result);
            assertEquals(mockDto, result);
            assertEquals(Date.valueOf(LocalDate.parse(startDate)), mockMonitoringEntity.getStartDate());
            assertEquals(Date.valueOf(LocalDate.parse(endDate)), mockMonitoringEntity.getEndDate());
            verify(playerMonitoringRepository, times(1)).save(mockMonitoringEntity);
        }
    }
}
