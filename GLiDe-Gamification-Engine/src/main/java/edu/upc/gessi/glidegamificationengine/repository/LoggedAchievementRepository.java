package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.LoggedAchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedAchievementRepository extends JpaRepository<LoggedAchievementEntity, Long> {
}
