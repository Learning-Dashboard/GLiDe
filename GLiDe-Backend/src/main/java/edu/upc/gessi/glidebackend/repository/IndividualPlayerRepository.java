package edu.upc.gessi.glidebackend.repository;

import edu.upc.gessi.glidebackend.entity.IndividualPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualPlayerRepository extends JpaRepository<IndividualPlayerEntity, String> {
}
