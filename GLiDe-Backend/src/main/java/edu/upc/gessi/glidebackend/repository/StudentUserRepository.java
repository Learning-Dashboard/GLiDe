package edu.upc.gessi.glidebackend.repository;

import edu.upc.gessi.glidebackend.entity.StudentUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentUserRepository extends JpaRepository<StudentUserEntity, String> {
}
