package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.GameEntity;
import edu.upc.gessi.glidegamificationengine.entity.SimpleRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimpleRuleRepository extends JpaRepository<SimpleRuleEntity, Long> {

    List<SimpleRuleEntity> findByGameEntity(GameEntity gameEntity);

}
