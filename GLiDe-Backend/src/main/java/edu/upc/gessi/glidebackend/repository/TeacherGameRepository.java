package edu.upc.gessi.glidebackend.repository;

import edu.upc.gessi.glidebackend.entity.TeacherGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherGameRepository extends JpaRepository<TeacherGameEntity, Long> {
    
    List<TeacherGameEntity> findByTeacherUserEntity_Email(String teacherEmail);
    
    List<TeacherGameEntity> findByGameSubjectAcronymAndGameCourseAndGamePeriod(
            String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
    
    Optional<TeacherGameEntity> findByTeacherUserEntity_EmailAndGameSubjectAcronymAndGameCourseAndGamePeriod(
            String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
    
    boolean existsByTeacherUserEntity_EmailAndGameSubjectAcronymAndGameCourseAndGamePeriod(
            String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
    
    void deleteByTeacherUserEntity_Email(String teacherEmail);
}