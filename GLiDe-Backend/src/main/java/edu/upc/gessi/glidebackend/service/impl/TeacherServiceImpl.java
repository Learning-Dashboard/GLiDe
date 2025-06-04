package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.dto.TeacherGameDto;
import edu.upc.gessi.glidebackend.dto.TeacherUserDto;
import edu.upc.gessi.glidebackend.entity.TeacherGameEntity;
import edu.upc.gessi.glidebackend.entity.TeacherUserEntity;
import edu.upc.gessi.glidebackend.mapper.TeacherUserMapper;
import edu.upc.gessi.glidebackend.repository.TeacherGameRepository;
import edu.upc.gessi.glidebackend.repository.TeacherUserRepository;
import edu.upc.gessi.glidebackend.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private TeacherGameRepository teacherGameRepository;
    private TeacherUserRepository teacherUserRepository;

    @Override
    public List<TeacherGameDto> getTeacherGames(String teacherEmail) {
        List<TeacherGameEntity> entities = teacherGameRepository.findByTeacherUserEntityEmail(teacherEmail);
        return TeacherUserMapper.mapToTeacherGameDtoList(entities);
    }

    @Override
    public TeacherGameDto assignGameToTeacher(String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        // Verificar que el professor existeix
        TeacherUserEntity teacher = teacherUserRepository.findById(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Verificar que l'assignació no existeix ja
        if (teacherGameRepository.existsByTeacherUserEntityEmailAndGameSubjectAcronymAndGameCourseAndGamePeriod(
                teacherEmail, gameSubjectAcronym, gameCourse, gamePeriod)) {
            throw new RuntimeException("Teacher already assigned to this game");
        }

        TeacherGameEntity entity = new TeacherGameEntity();
        entity.setTeacherUserEntity(teacher);
        entity.setGameSubjectAcronym(gameSubjectAcronym);
        entity.setGameCourse(gameCourse);
        entity.setGamePeriod(gamePeriod);

        TeacherGameEntity saved = teacherGameRepository.save(entity);
        return TeacherUserMapper.mapToTeacherGameDto(saved);
    }

    @Override
    public void removeGameFromTeacher(String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        List<TeacherGameEntity> entities = teacherGameRepository.findByTeacherUserEntityEmail(teacherEmail);
        entities.stream()
                .filter(entity -> entity.getGameSubjectAcronym().equals(gameSubjectAcronym) &&
                                entity.getGameCourse().equals(gameCourse) &&
                                entity.getGamePeriod().equals(gamePeriod))
                .findFirst()
                .ifPresent(teacherGameRepository::delete);
    }

    @Override
    public List<TeacherUserDto> getTeachersForGame(String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        List<TeacherGameEntity> entities = teacherGameRepository.findByGameSubjectAcronymAndGameCourseAndGamePeriod(
                gameSubjectAcronym, gameCourse, gamePeriod);
        
        List<TeacherUserEntity> teachers = entities.stream()
                .map(TeacherGameEntity::getTeacherUserEntity)
                .distinct()
                .toList();
                
        return TeacherUserMapper.mapToTeacherUserDtoList(teachers);
    }
}