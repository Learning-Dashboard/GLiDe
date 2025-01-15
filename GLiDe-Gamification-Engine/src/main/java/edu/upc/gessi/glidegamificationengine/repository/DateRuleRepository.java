package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.DateRuleEntity;
import edu.upc.gessi.glidegamificationengine.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateRuleRepository extends JpaRepository<DateRuleEntity, Long> {

    List<DateRuleEntity> findByGameEntity(GameEntity gameEntity);

}
