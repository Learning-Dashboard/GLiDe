package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.PlayerPerformanceLoggedActionEntity;
import edu.upc.gessi.glidegamificationengine.entity.key.LoggedActionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerPerformanceLoggedActionRepository extends JpaRepository<PlayerPerformanceLoggedActionEntity, LoggedActionKey> {
}
