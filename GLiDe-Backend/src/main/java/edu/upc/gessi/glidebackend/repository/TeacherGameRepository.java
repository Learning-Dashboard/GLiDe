package edu.upc.gessi.glidebackend.repository;

import edu.upc.gessi.glidebackend.entity.TeacherGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherGameRepository extends JpaRepository<TeacherGameEntity, Long> {
    
    List<TeacherGameEntity> findByTeacherUserEntityEmail(String teacherEmail);
    
    List<TeacherGameEntity> findByGameSubjectAcronymAndGameCourseAndGamePeriod(
        String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
    
    boolean existsByTeacherUserEntityEmailAndGameSubjectAcronymAndGameCourseAndGamePeriod(
        String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
}