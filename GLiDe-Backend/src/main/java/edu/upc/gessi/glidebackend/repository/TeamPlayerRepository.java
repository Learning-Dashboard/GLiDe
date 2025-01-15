package edu.upc.gessi.glidebackend.repository;

import edu.upc.gessi.glidebackend.entity.TeamPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayerEntity, String> {
}
