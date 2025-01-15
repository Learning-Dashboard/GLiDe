package edu.upc.gessi.glidegamificationengine.mapper;

import edu.upc.gessi.glidegamificationengine.dto.SubjectDTO;
import edu.upc.gessi.glidegamificationengine.entity.SubjectEntity;
import org.modelmapper.ModelMapper;

import javax.security.auth.Subject;

public class SubjectMapper {

    public static SubjectDTO mapToSubjectDto(SubjectEntity subjectEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(subjectEntity, SubjectDTO.class);
    }
}
