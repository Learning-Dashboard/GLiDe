package edu.upc.gessi.glidebackend.mapper;

import edu.upc.gessi.glidebackend.dto.PlayerGamificationDto;
import edu.upc.gessi.glidebackend.entity.PlayerGamificationEntity;
import org.modelmapper.ModelMapper;


public class PlayerGamificationMapper {
    public static PlayerGamificationDto mapToPlayerGamificationDto(PlayerGamificationEntity playerGamificationEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(playerGamificationEntity, PlayerGamificationDto.class);
    }
}
