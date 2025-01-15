package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.GameEntity;
import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, GameKey> {

    List<GameEntity> findByIdSubjectAcronym(String subjectAcronym);

    List<GameEntity> findByIdCourse(Integer course);

    List<GameEntity> findByIdPeriod(PeriodType period);

    List<GameEntity> findByIdSubjectAcronymAndIdCourse(String subjectAcronym, Integer course);

    List<GameEntity> findByIdSubjectAcronymAndIdPeriod(String subjectAcronym, PeriodType period);

    List<GameEntity> findByIdCourseAndIdPeriod(Integer course, PeriodType period);

}
