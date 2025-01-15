package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.LoggedActionEntity;
import edu.upc.gessi.glidegamificationengine.entity.key.LoggedActionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedActionRepository extends JpaRepository<LoggedActionEntity, LoggedActionKey> {
}
