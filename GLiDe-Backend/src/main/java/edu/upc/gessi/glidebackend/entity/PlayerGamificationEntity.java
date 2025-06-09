package edu.upc.gessi.glidebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import edu.upc.gessi.glidebackend.type.PeriodType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gamification_dashboard")
public class PlayerGamificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playername", referencedColumnName = "playername", nullable = false)
    private IndividualPlayerEntity individualPlayerEntity;

    @Column(name = "gameSubjectAcronym")
    private String gameSubjectAcronym;

    @Column(name = "gameCourse")
    private Integer gameCourse;

    @Enumerated(EnumType.STRING)
    @Column(name = "gamePeriod")
    private PeriodType gamePeriod;


}