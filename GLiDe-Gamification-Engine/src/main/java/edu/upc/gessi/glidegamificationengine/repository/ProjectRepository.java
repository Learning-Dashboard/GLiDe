package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.ProjectEntity;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("""
        SELECT p FROM ProjectEntity p
        WHERE p.name = :name
        AND p.gameGroupEntity.gameEntity.id.subjectAcronym = :gameSubjectAcronym
        AND p.gameGroupEntity.gameEntity.id.course = :gameCourse
        AND p.gameGroupEntity.gameEntity.id.period = :gamePeriod
    """)
    Optional<ProjectEntity> findByCustomQuery(
            @Param("name") String name,
            @Param("gameSubjectAcronym") String gameSubjectAcronym,
            @Param("gameCourse") Integer gameCourse,
            @Param("gamePeriod") PeriodType gamePeriod);
}
