package edu.upc.gessi.glidebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student_user")
public class StudentUserEntity {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "learningdashboard_username", nullable = false)
    private String learningdashboardUsername;

    @Column(name = "github_username", nullable = false)
    private String githubUsername;

    @Column(name = "taiga_username", nullable = false)
    private String taigaUsername;

    @Column(name = "nickname", nullable = true)
    private String nickname;

    @OneToMany(mappedBy = "studentUserEntity", fetch = FetchType.LAZY)
    private List<IndividualPlayerEntity> individualPlayerEntities;

}
