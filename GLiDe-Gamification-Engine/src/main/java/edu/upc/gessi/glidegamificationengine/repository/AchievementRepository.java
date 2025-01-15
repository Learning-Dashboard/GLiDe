package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementEntity, Long> {
}
