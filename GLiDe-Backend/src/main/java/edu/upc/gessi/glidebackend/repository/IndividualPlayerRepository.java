package edu.upc.gessi.glidebackend.repository;

import edu.upc.gessi.glidebackend.entity.IndividualPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import edu.upc.gessi.glidebackend.type.PeriodType;


import java.util.List;

@Repository
public interface IndividualPlayerRepository extends JpaRepository<IndividualPlayerEntity, String> {
    @Query("""
        SELECT pg.individualPlayerEntity FROM PlayerGamificationEntity pg
        WHERE pg.gameSubjectAcronym = :subjectAcronym
        AND pg.gameCourse = :course
        AND pg.gamePeriod = :period
    """)
    List<IndividualPlayerEntity> findByGameParameters(
            @Param("subjectAcronym") String subjectAcronym,
            @Param("course") Integer course,
            @Param("period") PeriodType period);
    
    List<IndividualPlayerEntity> findAll();

}
