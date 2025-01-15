package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.SubjectDTO;
import edu.upc.gessi.glidegamificationengine.entity.SubjectEntity;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.mapper.SubjectMapper;
import edu.upc.gessi.glidegamificationengine.repository.SubjectRepository;
import edu.upc.gessi.glidegamificationengine.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<SubjectDTO> getSubjects(){
        List<SubjectEntity> subjectEntities = subjectRepository.findAll();
        return subjectEntities.stream().map((SubjectMapper::mapToSubjectDto))
                .collect(Collectors.toList());
    }

    @Override
    public SubjectDTO createSubject(String acronym, Integer code, String name, String school, String studies){
        SubjectEntity subjectEntity = new SubjectEntity();
        if (acronym.isBlank())
            throw new ConstraintViolationException("Subject acronym cannot be blank.");
        subjectEntity.setAcronym(acronym);
        subjectEntity.setCode(code);
        subjectEntity.setName(name);
        subjectEntity.setSchool(school);
        subjectEntity.setStudies(studies);

        SubjectEntity savedSubjectEntity;

        if (subjectRepository.existsById(subjectEntity.getAcronym())) {
            throw new ConstraintViolationException("Subject already exists.");
        }

        try {
            savedSubjectEntity = subjectRepository.save(subjectEntity);
        }
        catch (Exception exception) {
            if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException)
                throw new ConstraintViolationException("Subject with the same code or name exists.");
            else throw exception;
        }
        return SubjectMapper.mapToSubjectDto(savedSubjectEntity);
    }
}
