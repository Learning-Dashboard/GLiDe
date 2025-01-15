package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.AchievementAssignmentEntity;
import edu.upc.gessi.glidegamificationengine.entity.key.AchievementAssignmentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementAssignmentRepository extends JpaRepository<AchievementAssignmentEntity, AchievementAssignmentKey> {
}
