package edu.upc.gessi.glidegamificationengine.entity;

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
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "learningdashboard_identifier", nullable = false)
    private String learningdashboardIdentifier;

    @Column(name = "github_identifier", nullable = false)
    private String githubIdentifier;

    @Column(name = "taiga_identifier", nullable = false)
    private String taigaIdentifier;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "game_group_subject_acronym", referencedColumnName = "game_subject_acronym", nullable = false),
            @JoinColumn(name = "game_group_course", referencedColumnName = "game_course", nullable = false),
            @JoinColumn(name = "game_group_period", referencedColumnName = "game_period", nullable = false),
            @JoinColumn(name = "game_group_group", referencedColumnName = "group", nullable = false)})
    private GameGroupEntity gameGroupEntity;

    @OneToOne(mappedBy = "projectEntity", fetch = FetchType.LAZY, optional = false)
    private TeamPlayerEntity teamPlayerEntity;

}
