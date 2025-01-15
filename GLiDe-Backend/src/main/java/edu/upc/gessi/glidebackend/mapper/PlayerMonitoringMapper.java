package edu.upc.gessi.glidebackend.mapper;

import edu.upc.gessi.glidebackend.dto.PlayerMonitoringDto;
import edu.upc.gessi.glidebackend.entity.PlayerMonitoringEntity;
import org.modelmapper.ModelMapper;

public class PlayerMonitoringMapper {
    public static PlayerMonitoringDto mapToPlayerMonitoringDto(PlayerMonitoringEntity playerMonitoringEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(playerMonitoringEntity, PlayerMonitoringDto.class);
    }
}
