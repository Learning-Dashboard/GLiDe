package edu.upc.gessi.glidegamificationengine.mapper;

import edu.upc.gessi.glidegamificationengine.dto.GameDTO;
import edu.upc.gessi.glidegamificationengine.entity.GameEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class GameMapper {

    public static GameDTO mapToGameDto(GameEntity gameEntity){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<GameEntity, GameDTO>() {
            @Override
            protected void configure() {
                using(ctx -> ((GameEntity) ctx.getSource()).getId().getSubjectAcronym())
                        .map(source, destination.getSubjectAcronym());
                using(ctx -> ((GameEntity) ctx.getSource()).getId().getCourse())
                        .map(source, destination.getCourse());
                using(ctx -> ((GameEntity) ctx.getSource()).getId().getPeriod())
                        .map(source, destination.getPeriod());
            }
        });
        return modelMapper.map(gameEntity, GameDTO.class);
    }

}
