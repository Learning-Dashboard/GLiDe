package edu.upc.gessi.glidegamificationengine.mapper;

import edu.upc.gessi.glidegamificationengine.dto.AchievementDTO;
import edu.upc.gessi.glidegamificationengine.entity.AchievementEntity;
import edu.upc.gessi.glidegamificationengine.type.AchievementCategoryType;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

public class AchievementMapper {

    public static AchievementDTO mapToAchievementDto(AchievementEntity achievementEntity){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(achievementEntity, AchievementDTO.class);
    }

    public static AchievementEntity mapToAchievementEntity(AchievementDTO achievementDto){
        ModelMapper modelMapper = new ModelMapper();
        Converter<String, AchievementCategoryType> stringToAchievementCategoryType = ctx -> AchievementCategoryType.fromString(ctx.getSource());
        TypeMap<AchievementDTO, AchievementEntity> propertyMapper = modelMapper.createTypeMap(AchievementDTO.class, AchievementEntity.class);
        propertyMapper.addMappings(mapper -> {
            mapper.using(stringToAchievementCategoryType).map(AchievementDTO::getCategory, AchievementEntity::setCategory);
        });
        return modelMapper.map(achievementDto, AchievementEntity.class);
    }

}
