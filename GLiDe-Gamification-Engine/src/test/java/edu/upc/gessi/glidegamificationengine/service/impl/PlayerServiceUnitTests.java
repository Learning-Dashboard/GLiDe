package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.IndividualPlayerDTO;
import edu.upc.gessi.glidegamificationengine.dto.PlayerAchievementDTO;
import edu.upc.gessi.glidegamificationengine.dto.PlayerLoggedAchievementDTO;
import edu.upc.gessi.glidegamificationengine.dto.TeamPlayerDTO;
import edu.upc.gessi.glidegamificationengine.entity.*;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.mapper.PlayerMapper;
import edu.upc.gessi.glidegamificationengine.repository.IndividualPlayerRepository;
import edu.upc.gessi.glidegamificationengine.repository.PlayerRepository;
import edu.upc.gessi.glidegamificationengine.repository.TeamPlayerRepository;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceUnitTests {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamPlayerRepository teamPlayerRepository;

    @Mock
    private IndividualPlayerRepository individualPlayerRepository;

    @Mock
    private LoggedAchievementServiceImpl loggedAchievementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTeamPlayer_Success() {
        String teamPlayerPlayername = "teamPlayer1";
        TeamPlayerEntity mockEntity = new TeamPlayerEntity();
        mockEntity.setPlayername(teamPlayerPlayername);

        when(teamPlayerRepository.findById(teamPlayerPlayername)).thenReturn(Optional.of(mockEntity));
        try (MockedStatic<PlayerMapper> mockedMapper = mockStatic(PlayerMapper.class)) {
            mockedMapper.when(() -> PlayerMapper.mapToTeamPlayerDto(mockEntity)).thenReturn(new TeamPlayerDTO());

            TeamPlayerDTO result = playerService.getTeamPlayer(teamPlayerPlayername);

            assertNotNull(result);
            verify(teamPlayerRepository, times(1)).findById(teamPlayerPlayername);
        }
    }

    @Test
    void testGetTeamPlayer_NotFound() {
        String teamPlayerPlayername = "nonexistent";

        when(teamPlayerRepository.findById(teamPlayerPlayername)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getTeamPlayer(teamPlayerPlayername));
        verify(teamPlayerRepository, times(1)).findById(teamPlayerPlayername);
    }

    @Test
    void testGetIndividualPlayer_Success() {
        String individualPlayerPlayername = "individualPlayer1";
        IndividualPlayerEntity mockEntity = new IndividualPlayerEntity();
        mockEntity.setPlayername(individualPlayerPlayername);

        when(individualPlayerRepository.findById(individualPlayerPlayername)).thenReturn(Optional.of(mockEntity));
        try (MockedStatic<PlayerMapper> mockedMapper = mockStatic(PlayerMapper.class)) {
            mockedMapper.when(() -> PlayerMapper.mapToIndividualPlayerDto(mockEntity))
                    .thenReturn(new IndividualPlayerDTO());

            IndividualPlayerDTO result = playerService.getIndividualPlayer(individualPlayerPlayername);

            assertNotNull(result);
            verify(individualPlayerRepository, times(1)).findById(individualPlayerPlayername);
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
    void testUpdatePlayersPointsAndLevels() {
        List<PlayerEntity> mockPlayers = List.of(mock(PlayerEntity.class), mock(PlayerEntity.class));

        when(playerRepository.findAll()).thenReturn(mockPlayers);

        playerService.updatePlayersPointsAndLevels();

        for (PlayerEntity player : mockPlayers) {
            verify(player, times(1)).updatePoints();
            verify(player, times(1)).updateLevel();
        }
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void testSetPlayerLoggedAchievementViewed_PlayerNotFound() {
        String playerPlayername = "nonexistent";
        Long loggedAchievementId = 1L;

        when(playerRepository.findById(playerPlayername)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.setPlayerLoggedAchievementViewed(playerPlayername, loggedAchievementId, true));
        verify(playerRepository, times(1)).findById(playerPlayername);
    }

    @Test
    void testGetPlayerAchievements_WithCategory() {
        String playerPlayername = "player1";
        String achievementCategory = "Points";
        TeamPlayerEntity mockPlayer = mock(TeamPlayerEntity.class);

        when(playerRepository.findById(playerPlayername)).thenReturn(Optional.of(mockPlayer));
        when(mockPlayer.getType()).thenReturn(PlayerType.Team);
        when(mockPlayer.getLoggedAchievementEntities(any())).thenReturn(Collections.emptyList());

        List<PlayerAchievementDTO> result = playerService.getPlayerAchievements(playerPlayername, true, achievementCategory);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(playerRepository, times(1)).findById(playerPlayername);
    }

    @Test
    void testGetPlayerLoggedAchievements_WithCategory() {
        String playerPlayername = "player1";
        String achievementCategory = "Points";
        TeamPlayerEntity mockPlayer = mock(TeamPlayerEntity.class);

        when(playerRepository.findById(playerPlayername)).thenReturn(Optional.of(mockPlayer));
        when(mockPlayer.getType()).thenReturn(PlayerType.Team);
        when(mockPlayer.getLoggedAchievementEntities(any())).thenReturn(Collections.emptyList());

        List<PlayerLoggedAchievementDTO> result = playerService.getPlayerLoggedAchievements(playerPlayername, true, achievementCategory);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(playerRepository, times(1)).findById(playerPlayername);
    }

    @Test
    void testSetPlayerLoggedAchievementViewed() {
        String playerPlayername = "player1";
        Long loggedAchievementId = 123L;
        Boolean viewed = true;

        TeamPlayerEntity mockPlayer = mock(TeamPlayerEntity.class);
        LoggedAchievementEntity mockLoggedAchievement = mock(LoggedAchievementEntity.class);
        AchievementAssignmentEntity mockAchievementAssignment = mock(AchievementAssignmentEntity.class);
        AchievementEntity mockAchievementEntity = mock(AchievementEntity.class);

        when(playerRepository.findById(playerPlayername)).thenReturn(Optional.of(mockPlayer));
        when(mockPlayer.getType()).thenReturn(PlayerType.Team);
        when(loggedAchievementService.getLoggedAchievementEntityById(loggedAchievementId)).thenReturn(mockLoggedAchievement);
        when(mockLoggedAchievement.getAchievementAssignmentEntity()).thenReturn(mockAchievementAssignment);
        when(mockAchievementAssignment.getAchievementEntity()).thenReturn(mockAchievementEntity);

        PlayerLoggedAchievementDTO result = playerService.setPlayerLoggedAchievementViewed(playerPlayername, loggedAchievementId, viewed);

        assertNotNull(result);
        verify(mockLoggedAchievement, times(1)).setViewed(viewed);
        verify(playerRepository, times(1)).findById(playerPlayername);
    }
}
