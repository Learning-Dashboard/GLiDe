package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.dto.IndividualPlayerDto;
import edu.upc.gessi.glidebackend.dto.StudentUserDto;
import edu.upc.gessi.glidebackend.entity.IndividualPlayerEntity;
import edu.upc.gessi.glidebackend.entity.StudentUserEntity;
import edu.upc.gessi.glidebackend.exception.ResourceNotFoundException;
import edu.upc.gessi.glidebackend.exception.TypeNotCorrectException;
import edu.upc.gessi.glidebackend.mapper.PlayerMapper;
import edu.upc.gessi.glidebackend.mapper.StudentUserMapper;
import edu.upc.gessi.glidebackend.repository.IndividualPlayerRepository;
import edu.upc.gessi.glidebackend.repository.StudentUserRepository;
import edu.upc.gessi.glidebackend.type.PeriodType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceUnitTests {    @Mock
    private StudentUserRepository studentUserRepository;

    @Mock
    private IndividualPlayerRepository individualPlayerRepository;

    @Mock
    private AuthServiceImpl authService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStudent_success() {
        String idToken = "validToken";
        String email = "test@student.com";
        StudentUserEntity mockEntity = new StudentUserEntity();
        mockEntity.setUsername(email);

        StudentUserDto mockDto = new StudentUserDto();
        mockDto.setUsername(email);

        when(authService.getTokenMail(idToken)).thenReturn(email);
        when(studentUserRepository.findById(email)).thenReturn(java.util.Optional.of(mockEntity));
        try (MockedStatic<StudentUserMapper> mockedMapper = mockStatic(StudentUserMapper.class)) {
            mockedMapper.when(() -> StudentUserMapper.mapToStudentUserDto(mockEntity))
                    .thenReturn(mockDto);

            StudentUserDto result = studentService.getStudent(idToken);

            assertNotNull(result);
            assertEquals(mockDto.getUsername(), result.getUsername());
            verify(authService, times(1)).getTokenMail(idToken);
            verify(studentUserRepository, times(1)).findById(email);
        }
    }

    @Test
    void testGetStudent_notFound() {
        // Given
        String idToken = "validToken";
        String email = "test@student.com";

        when(authService.getTokenMail(idToken)).thenReturn(email);
        when(studentUserRepository.findById(email)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudent(idToken));
        verify(authService, times(1)).getTokenMail(idToken);
        verify(studentUserRepository, times(1)).findById(email);
    }

    @Test
    void testGetStudentPlayers_success() {
        String idToken = "validToken";
        String email = "test@student.com";

        StudentUserEntity mockStudentEntity = new StudentUserEntity();
        mockStudentEntity.setUsername(email);

        IndividualPlayerEntity mockPlayerEntity = new IndividualPlayerEntity();
        mockPlayerEntity.setPlayername("player1");

        IndividualPlayerDto mockPlayerDto = new IndividualPlayerDto();
        mockPlayerDto.setPlayername("player1");

        mockStudentEntity.setIndividualPlayerEntities(List.of(mockPlayerEntity));

        when(authService.getTokenMail(idToken)).thenReturn(email);
        when(studentUserRepository.findById(email)).thenReturn(java.util.Optional.of(mockStudentEntity));
        try (MockedStatic<PlayerMapper> mockedMapper = mockStatic(PlayerMapper.class)) {
            mockedMapper.when(() -> PlayerMapper.mapToIndividualPlayerDto(mockPlayerEntity))
                    .thenReturn(mockPlayerDto);

            List<IndividualPlayerDto> result = studentService.getStudentPlayers(idToken);

            assertNotNull(result);
            assertEquals(1, result.size());
            IndividualPlayerDto resultPlayer = result.getFirst();
            assertEquals(mockPlayerDto.getPlayername(), resultPlayer.getPlayername());
            verify(authService, times(1)).getTokenMail(idToken);
            verify(studentUserRepository, times(1)).findById(email);
        }
    }

    @Test
    void testGetStudentPlayers_notFound() {
        String idToken = "validToken";
        String email = "test@student.com";

        when(authService.getTokenMail(idToken)).thenReturn(email);
        when(studentUserRepository.findById(email)).thenReturn(java.util.Optional.empty());        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentPlayers(idToken));
        verify(authService, times(1)).getTokenMail(idToken);
        verify(studentUserRepository, times(1)).findById(email);
    }

    @Test
    void testGetStudentsByGame_success() {
        // Given
        String subjectAcronym = "SE";
        Integer course = 2024;
        String period = "Quadrimester1";

        IndividualPlayerEntity mockPlayerEntity1 = new IndividualPlayerEntity();
        mockPlayerEntity1.setPlayername("player1");
        
        IndividualPlayerEntity mockPlayerEntity2 = new IndividualPlayerEntity();
        mockPlayerEntity2.setPlayername("player2");

        IndividualPlayerDto mockPlayerDto1 = new IndividualPlayerDto();
        mockPlayerDto1.setPlayername("player1");
        
        IndividualPlayerDto mockPlayerDto2 = new IndividualPlayerDto();
        mockPlayerDto2.setPlayername("player2");

        List<IndividualPlayerEntity> mockEntities = List.of(mockPlayerEntity1, mockPlayerEntity2);

        when(individualPlayerRepository.findByGameParameters(subjectAcronym, course, PeriodType.fromString(period)))
                .thenReturn(mockEntities);

        try (MockedStatic<PlayerMapper> mockedMapper = mockStatic(PlayerMapper.class)) {
            mockedMapper.when(() -> PlayerMapper.mapToIndividualPlayerDto(mockPlayerEntity1))
                    .thenReturn(mockPlayerDto1);
            mockedMapper.when(() -> PlayerMapper.mapToIndividualPlayerDto(mockPlayerEntity2))
                    .thenReturn(mockPlayerDto2);

            // When
            List<IndividualPlayerDto> result = studentService.getStudentsByGame(subjectAcronym, course, period);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("player1", result.get(0).getPlayername());
            assertEquals("player2", result.get(1).getPlayername());
            verify(individualPlayerRepository, times(1))
                    .findByGameParameters(subjectAcronym, course, PeriodType.fromString(period));
        }
    }

    @Test
    void testGetStudentsByGame_emptyResult() {
        // Given
        String subjectAcronym = "SE";
        Integer course = 2024;
        String period = "Quadrimester1";

        when(individualPlayerRepository.findByGameParameters(subjectAcronym, course, PeriodType.fromString(period)))
                .thenReturn(List.of());

        // When
        List<IndividualPlayerDto> result = studentService.getStudentsByGame(subjectAcronym, course, period);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(individualPlayerRepository, times(1))
                .findByGameParameters(subjectAcronym, course, PeriodType.fromString(period));
    }

    @Test
    void testGetStudentsByGame_invalidPeriod() {
        // Given
        String subjectAcronym = "SE";
        Integer course = 2024;
        String period = "INVALID";

        // When & Then
        assertThrows(TypeNotCorrectException.class, () -> 
                studentService.getStudentsByGame(subjectAcronym, course, period));
    }
}
