package edu.upc.gessi.glidegamificationengine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student_user")
public class StudentUserEntity extends UserEntity {

    @Column(name = "learningdashboard_username", nullable = false)
    private String learningdashboardUsername;

    @Column(name = "github_username", nullable = false)
    private String githubUsername;

    @Column(name = "taiga_username", nullable = false)
    private String taigaUsername;

}
