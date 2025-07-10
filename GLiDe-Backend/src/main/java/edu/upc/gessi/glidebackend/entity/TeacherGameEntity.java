package edu.upc.gessi.glidebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacher_game")
public class TeacherGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_email", referencedColumnName = "email", nullable = false)
    private TeacherUserEntity teacherUserEntity;

    @Column(name = "game_subject_acronym", nullable = false)
    private String gameSubjectAcronym;

    @Column(name = "game_course", nullable = false)
    private Integer gameCourse;

    @Column(name = "game_period", nullable = false)
    private String gamePeriod;
}