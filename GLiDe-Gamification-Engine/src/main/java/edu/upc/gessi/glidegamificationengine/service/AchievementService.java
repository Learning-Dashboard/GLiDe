package edu.upc.gessi.glidegamificationengine.service;

import edu.upc.gessi.glidegamificationengine.dto.AchievementCategoryDTO;
import edu.upc.gessi.glidegamificationengine.dto.AchievementDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AchievementService {

    AchievementDTO createAchievement(String achievementName, MultipartFile achievementIcon, String achievementCategory) throws IOException;

    List<AchievementDTO> getAchievements();

    AchievementDTO getAchievement(Long achievementId);

    AchievementDTO updateAchievement(Long achievementId, String achievementName, MultipartFile achievementIcon, String achievementCategory) throws IOException;

    void deleteAchievement(Long achievementId);

    List<AchievementCategoryDTO> getAchievementCategories();

    AchievementCategoryDTO getAchievementCategory(String achievementCategoryName);

}
