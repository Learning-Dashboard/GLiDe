package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.AchievementCategoryDTO;
import edu.upc.gessi.glidegamificationengine.dto.AchievementDTO;
import edu.upc.gessi.glidegamificationengine.entity.AchievementEntity;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.exception.TypeNotCorrectException;
import edu.upc.gessi.glidegamificationengine.repository.AchievementRepository;
import edu.upc.gessi.glidegamificationengine.type.AchievementCategoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AchievementServiceUnitTests {

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private PlayerServiceImpl playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAchievement_ValidInput_Success() throws IOException {
        String achievementName = "Test Achievement";
        MockMultipartFile icon = new MockMultipartFile("icon", "icon.png", "image/png", "test image".getBytes());
        String achievementCategory = "Points";

        AchievementEntity mockEntity = new AchievementEntity();
        mockEntity.setName(achievementName);
        mockEntity.setIcon(Base64.getEncoder().encodeToString(icon.getBytes()));
        mockEntity.setCategory(AchievementCategoryType.Points);

        when(achievementRepository.save(any(AchievementEntity.class))).thenReturn(mockEntity);

        AchievementDTO result = achievementService.createAchievement(achievementName, icon, achievementCategory);

        assertNotNull(result);
        assertEquals(achievementName, result.getName());
        verify(achievementRepository, times(1)).save(any(AchievementEntity.class));
    }

    @Test
    void createAchievement_BlankName_ThrowsException() {
        MockMultipartFile icon = new MockMultipartFile("icon", "icon.png", "image/png", "test image".getBytes());
        String achievementCategory = "Points";

        assertThrows(ConstraintViolationException.class, () ->
                achievementService.createAchievement("", icon, achievementCategory));
    }

    @Test
    void getAchievements_ReturnsAchievementList() {
        List<AchievementEntity> mockEntities = new ArrayList<>();
        AchievementEntity mockEntity = new AchievementEntity();
        mockEntity.setName("Achievement 1");
        mockEntities.add(mockEntity);

        when(achievementRepository.findAll()).thenReturn(mockEntities);

        List<AchievementDTO> result = achievementService.getAchievements();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void getAchievement_ExistingId_ReturnsAchievement() {
        Long achievementId = 1L;
        AchievementEntity mockEntity = new AchievementEntity();
        mockEntity.setId(achievementId);
        mockEntity.setName("Achievement 1");

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(mockEntity));

        AchievementDTO result = achievementService.getAchievement(achievementId);

        assertNotNull(result);
        assertEquals("Achievement 1", result.getName());
        verify(achievementRepository, times(1)).findById(achievementId);
    }

    @Test
    void getAchievement_NonExistingId_ThrowsException() {
        Long achievementId = 1L;

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                achievementService.getAchievement(achievementId));
    }

    @Test
    void updateAchievement_ValidInput_Success() throws IOException {
        Long achievementId = 1L;
        String newName = "Updated Achievement";
        MockMultipartFile newIcon = new MockMultipartFile("icon", "icon.png", "image/png", "updated image".getBytes());
        String newCategory = "Points";

        AchievementEntity mockEntity = new AchievementEntity();
        mockEntity.setId(achievementId);
        mockEntity.setName("Old Name");
        mockEntity.setCategory(AchievementCategoryType.Points);

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(mockEntity));
        when(achievementRepository.save(any(AchievementEntity.class))).thenReturn(mockEntity);

        AchievementDTO result = achievementService.updateAchievement(achievementId, newName, newIcon, newCategory);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        verify(achievementRepository, times(1)).save(any(AchievementEntity.class));
    }

    @Test
    void deleteAchievement_ValidId_Success() {
        Long achievementId = 1L;
        AchievementEntity mockEntity = new AchievementEntity();
        mockEntity.setId(achievementId);
        mockEntity.setCategory(AchievementCategoryType.Points);

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(mockEntity));

        achievementService.deleteAchievement(achievementId);

        verify(achievementRepository, times(1)).deleteById(achievementId);
        verify(playerService, times(1)).updatePlayersPointsAndLevels();
    }

    @Test
    void getAchievementCategories_ReturnsAllCategories() {
        List<AchievementCategoryDTO> result = achievementService.getAchievementCategories();

        assertNotNull(result);
        assertEquals(AchievementCategoryType.values().length, result.size());
    }

    @Test
    void getAchievementCategory_ValidCategory_ReturnsCategory() {
        String categoryName = "Points";
        String description = "They represent the basic unit to measure the players' evolution and determine the players' level";

        AchievementCategoryDTO result = achievementService.getAchievementCategory(categoryName);

        assertNotNull(result);
        assertEquals(description, result.getDescription());
    }

    @Test
    void getAchievementCategory_InvalidCategory_ThrowsException() {
        assertThrows(TypeNotCorrectException.class, () ->
                achievementService.getAchievementCategory("InvalidCategory"));
    }
}
