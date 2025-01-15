package edu.upc.gessi.glidegamificationengine.mapper;

import edu.upc.gessi.glidegamificationengine.dto.EvaluableActionDTO;
import edu.upc.gessi.glidegamificationengine.entity.EvaluableActionEntity;
import org.modelmapper.ModelMapper;

public class EvaluableActionMapper {

    public static EvaluableActionDTO mapToEvaluableActionDto(EvaluableActionEntity evaluableActionEntity){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(evaluableActionEntity, EvaluableActionDTO.class);
    }

}
