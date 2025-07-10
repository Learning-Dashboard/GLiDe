package edu.upc.gessi.glidebackend.repository;

import edu.upc.gessi.glidebackend.entity.TeacherUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherUserRepository extends JpaRepository<TeacherUserEntity, String> {
}