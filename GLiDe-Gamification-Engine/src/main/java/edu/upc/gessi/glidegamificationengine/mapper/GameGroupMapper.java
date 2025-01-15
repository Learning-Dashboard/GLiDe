package edu.upc.gessi.glidegamificationengine.mapper;

import edu.upc.gessi.glidegamificationengine.dto.GameGroupDTO;
import edu.upc.gessi.glidegamificationengine.entity.GameGroupEntity;
import org.modelmapper.ModelMapper;

public class GameGroupMapper {

    public static GameGroupDTO mapToGameGroupDTO(GameGroupEntity gameGroupEntity){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(gameGroupEntity, GameGroupDTO.class);
    }
}
