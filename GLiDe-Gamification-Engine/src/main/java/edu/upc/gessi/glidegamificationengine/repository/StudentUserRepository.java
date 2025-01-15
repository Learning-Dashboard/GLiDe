package edu.upc.gessi.glidegamificationengine.repository;

import edu.upc.gessi.glidegamificationengine.entity.StudentUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentUserRepository extends JpaRepository<StudentUserEntity, String> {
}
