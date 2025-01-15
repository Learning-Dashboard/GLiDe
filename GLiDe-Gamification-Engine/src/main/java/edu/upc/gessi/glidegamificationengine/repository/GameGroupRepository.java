package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.GameGroupEntity;
import edu.upc.gessi.glidegamificationengine.entity.key.GameGroupKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameGroupRepository extends JpaRepository<GameGroupEntity, GameGroupKey> {
}
