package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.dto.IndividualPlayerDto;
import edu.upc.gessi.glidebackend.dto.StudentUserDto;
import edu.upc.gessi.glidebackend.entity.IndividualPlayerEntity;
import edu.upc.gessi.glidebackend.entity.StudentUserEntity;
import edu.upc.gessi.glidebackend.excpetion.ResourceNotFoundException;
import edu.upc.gessi.glidebackend.mapper.PlayerMapper;
import edu.upc.gessi.glidebackend.mapper.StudentUserMapper;
import edu.upc.gessi.glidebackend.repository.StudentUserRepository;
import edu.upc.gessi.glidebackend.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentUserRepository studentUserRepository;
    @Autowired
    private AuthServiceImpl authService;

    @Override
    @Transactional
    public StudentUserDto getStudent(String idToken){
        String email = authService.getTokenMail(idToken);
        StudentUserEntity studentUserEntity = studentUserRepository.findById(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return StudentUserMapper.mapToStudentUserDto(studentUserEntity);
    }

    @Override
    @Transactional
    public List<IndividualPlayerDto> getStudentPlayers(String idToken){
        String email = authService.getTokenMail(idToken);
        StudentUserEntity studentUserEntity = studentUserRepository.findById(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        List<IndividualPlayerEntity> individualPlayerEntities = studentUserEntity.getIndividualPlayerEntities();
        return individualPlayerEntities.stream().map(PlayerMapper::mapToIndividualPlayerDto)
                .collect(Collectors.toList());
    }
}
