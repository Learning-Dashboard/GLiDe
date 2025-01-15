package edu.upc.gessi.glidegamificationengine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subject")
public class SubjectEntity {

    @Id
    @Column(name = "acronym")
    private String acronym;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "code", unique = true, nullable = false)
    private Integer code;

    @Column(name = "studies", nullable = false)
    private String studies;

    @Column(name = "school", nullable = false)
    private String school;

}
