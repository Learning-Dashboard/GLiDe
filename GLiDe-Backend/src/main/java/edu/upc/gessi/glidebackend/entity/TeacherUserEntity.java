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
@Table(name = "teacher_user")
public class TeacherUserEntity {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @OneToMany(mappedBy = "teacherUserEntity", fetch = FetchType.LAZY)
    private List<TeacherGameEntity> teacherGameEntities;
}
