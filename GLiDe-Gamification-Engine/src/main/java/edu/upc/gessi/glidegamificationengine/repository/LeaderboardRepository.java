package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.GameEntity;
import edu.upc.gessi.glidegamificationengine.entity.LeaderboardEntity;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntity, Long> {

    public List<LeaderboardEntity> findByGameEntity(GameEntity gameEntity);

}
