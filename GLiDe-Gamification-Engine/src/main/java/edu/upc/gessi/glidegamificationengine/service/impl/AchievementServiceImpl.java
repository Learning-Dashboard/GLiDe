package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.AchievementCategoryDTO;
import edu.upc.gessi.glidegamificationengine.dto.AchievementDTO;
import edu.upc.gessi.glidegamificationengine.entity.AchievementEntity;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.mapper.AchievementMapper;
import edu.upc.gessi.glidegamificationengine.repository.AchievementRepository;
import edu.upc.gessi.glidegamificationengine.service.AchievementService;
import edu.upc.gessi.glidegamificationengine.type.AchievementCategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private PlayerServiceImpl playerService;

    /* Methods callable from Service Layer */

    protected AchievementEntity getAchievementEntityById(Long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement with id '" + achievementId + "' not found."));
    }


    /* Methods callable from Controller Layer */

    @Override
    public AchievementDTO createAchievement(String achievementName, MultipartFile achievementIcon, String achievementCategory) throws IOException {
        AchievementEntity achievementEntity = new AchievementEntity();
        if (achievementName.isBlank())
            throw new ConstraintViolationException("Achievement name cannot be blank, please introduce a name.");
        achievementEntity.setName(achievementName);
        if (achievementIcon != null) achievementEntity.setIcon(Base64.getEncoder().encodeToString(achievementIcon.getBytes()));
        achievementEntity.setCategory(AchievementCategoryType.fromString(achievementCategory));

        AchievementEntity savedAchievementEntity;
        try{
             savedAchievementEntity = achievementRepository.save(achievementEntity);
        }
        catch (Exception exception){
            if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException)
                throw new ConstraintViolationException("Achievement name '" + achievementEntity.getName() + "' already used, please pick a different name.");
            else throw exception;
        }
        return AchievementMapper.mapToAchievementDto(savedAchievementEntity);
    }

    @Override
    public List<AchievementDTO> getAchievements() {
        List<AchievementEntity> achievementEntities = achievementRepository.findAll();
        return achievementEntities.stream().map((achievementEntity -> AchievementMapper.mapToAchievementDto(achievementEntity)))
                .collect(Collectors.toList());
    }

    @Override
    public AchievementDTO getAchievement(Long achievementId) {
        AchievementEntity achievementEntity = getAchievementEntityById(achievementId);
        return AchievementMapper.mapToAchievementDto(achievementEntity);
    }

    @Override
    public AchievementDTO updateAchievement(Long achievementId, String achievementName, MultipartFile achievementIcon, String achievementCategory) throws IOException {
        AchievementEntity achievementEntity = getAchievementEntityById(achievementId);
        if (achievementName.isBlank())
            throw new ConstraintViolationException("Achievement name cannot be blank, please introduce a name.");
        achievementEntity.setName(achievementName);
        if (achievementIcon != null) achievementEntity.setIcon(Base64.getEncoder().encodeToString(achievementIcon.getBytes()));
        else achievementEntity.setIcon(null);
        AchievementCategoryType achievementOldCategory = achievementEntity.getCategory();
        achievementEntity.setCategory(AchievementCategoryType.fromString(achievementCategory));
        AchievementCategoryType achievementNewCategory = achievementEntity.getCategory();

        AchievementEntity updatedAchievementEntity;
        try{
            updatedAchievementEntity = achievementRepository.save(achievementEntity);
        }
        catch (Exception exception){
            if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException)
                throw new ConstraintViolationException("Achievement name '" + achievementEntity.getName() + "' already used, please pick a different name.");
            else throw exception;
        }

        if (achievementNewCategory.equals(AchievementCategoryType.Points) && !achievementOldCategory.equals(AchievementCategoryType.Points))
            playerService.updatePlayersPointsAndLevels();

        return AchievementMapper.mapToAchievementDto(updatedAchievementEntity);
    }

    @Override
    public void deleteAchievement(Long achievementId) {
        AchievementEntity achievementEntity = getAchievementEntityById(achievementId);
        Boolean achievementCategoryIsPoints = achievementEntity.getCategory().equals(AchievementCategoryType.Points);

        achievementRepository.deleteById(achievementId);

        if (achievementCategoryIsPoints)
            playerService.updatePlayersPointsAndLevels();
    }

    @Override
    public List<AchievementCategoryDTO> getAchievementCategories() {
        List<AchievementCategoryDTO> achievementCategoryDtos = new ArrayList<>();
        for (AchievementCategoryType value : AchievementCategoryType.values()) {
            achievementCategoryDtos.add(new AchievementCategoryDTO(value, value.getDescription(), value.isNumerical()));
        }
        return achievementCategoryDtos;
    }

    @Override
    public AchievementCategoryDTO getAchievementCategory(String achievementCategoryName) {
        AchievementCategoryType achievementCategory = AchievementCategoryType.fromString(achievementCategoryName);
        return new AchievementCategoryDTO(achievementCategory, achievementCategory.getDescription(), achievementCategory.isNumerical());
    }

}
