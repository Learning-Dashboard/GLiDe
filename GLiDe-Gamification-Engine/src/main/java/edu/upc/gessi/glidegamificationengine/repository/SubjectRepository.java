package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {
}
