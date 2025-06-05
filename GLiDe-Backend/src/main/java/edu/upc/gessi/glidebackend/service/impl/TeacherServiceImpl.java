package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.dto.TeacherUserDto;
import edu.upc.gessi.glidebackend.dto.TeacherGameDto;
import edu.upc.gessi.glidebackend.entity.TeacherUserEntity;
import edu.upc.gessi.glidebackend.entity.TeacherGameEntity;
import edu.upc.gessi.glidebackend.repository.TeacherUserRepository;
import edu.upc.gessi.glidebackend.repository.TeacherGameRepository;
import edu.upc.gessi.glidebackend.service.TeacherService;
import edu.upc.gessi.glidebackend.excpetion.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherUserRepository teacherUserRepository;

    @Autowired
    private TeacherGameRepository teacherGameRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TeacherUserDto> getAllTeachers() {
        return teacherUserRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, TeacherUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TeacherUserDto getTeacher(String email) {
        TeacherUserEntity entity = teacherUserRepository.findById(email)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with email: " + email));
        return modelMapper.map(entity, TeacherUserDto.class);
    }

    @Override
    public TeacherUserDto createTeacher(TeacherUserDto teacherDto) {
        if (teacherUserRepository.existsById(teacherDto.getEmail())) {
            throw new IllegalArgumentException("Teacher already exists with email: " + teacherDto.getEmail());
        }
        
        TeacherUserEntity entity = modelMapper.map(teacherDto, TeacherUserEntity.class);
        TeacherUserEntity saved = teacherUserRepository.save(entity);
        return modelMapper.map(saved, TeacherUserDto.class);
    }

    @Override
    public TeacherUserDto updateTeacher(String email, TeacherUserDto teacherDto) {
        TeacherUserEntity entity = teacherUserRepository.findById(email)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with email: " + email));
        
        entity.setName(teacherDto.getName());
        entity.setSurname(teacherDto.getSurname());
        
        TeacherUserEntity saved = teacherUserRepository.save(entity);
        return modelMapper.map(saved, TeacherUserDto.class);
    }

    @Override
    public void deleteTeacher(String email) {
        if (!teacherUserRepository.existsById(email)) {
            throw new ResourceNotFoundException("Teacher not found with email: " + email);
        }
        
        // Primer eliminar totes les assignacions de games
        teacherGameRepository.deleteByTeacherUserEntity_Email(email);
        
        // Després eliminar el professor
        teacherUserRepository.deleteById(email);
    }

    @Override
    public List<TeacherGameDto> getTeacherGames(String teacherEmail) {
        return teacherGameRepository.findByTeacherUserEntity_Email(teacherEmail).stream()
                .map(entity -> modelMapper.map(entity, TeacherGameDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TeacherGameDto assignGameToTeacher(String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        TeacherUserEntity teacher = teacherUserRepository.findById(teacherEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with email: " + teacherEmail));

        // Verificar si ja existeix l'assignació
        boolean exists = teacherGameRepository.existsByTeacherUserEntity_EmailAndGameSubjectAcronymAndGameCourseAndGamePeriod(
                teacherEmail, gameSubjectAcronym, gameCourse, gamePeriod);
        
        if (exists) {
            throw new IllegalArgumentException("Teacher is already assigned to this game");
        }

        TeacherGameEntity entity = new TeacherGameEntity();
        entity.setTeacherUserEntity(teacher);
        entity.setGameSubjectAcronym(gameSubjectAcronym);
        entity.setGameCourse(gameCourse);
        entity.setGamePeriod(gamePeriod);

        TeacherGameEntity saved = teacherGameRepository.save(entity);
        return modelMapper.map(saved, TeacherGameDto.class);
    }

    @Override
    public void removeGameFromTeacher(String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        TeacherGameEntity entity = teacherGameRepository.findByTeacherUserEntity_EmailAndGameSubjectAcronymAndGameCourseAndGamePeriod(
                teacherEmail, gameSubjectAcronym, gameCourse, gamePeriod)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher game assignment not found"));
        
        teacherGameRepository.delete(entity);
    }

    @Override
    public List<TeacherGameDto> getTeachersForGame(String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        return teacherGameRepository.findByGameSubjectAcronymAndGameCourseAndGamePeriod(
                gameSubjectAcronym, gameCourse, gamePeriod).stream()
                .map(entity -> modelMapper.map(entity, TeacherGameDto.class))
                .collect(Collectors.toList());
    }
}