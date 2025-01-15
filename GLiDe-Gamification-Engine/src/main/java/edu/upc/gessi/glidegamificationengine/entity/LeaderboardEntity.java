package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.type.AnonymizationType;
import edu.upc.gessi.glidegamificationengine.type.ExtentType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leaderboard")
@Check(constraints = "start_date <= end_date")
public class LeaderboardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "assessment_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerType assessmentLevel;

    @Column(name = "extent", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExtentType extent;

    @Column(name = "anonymization", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnonymizationType anonymization;

    @Column(name = "student_visible", nullable = false)
    private Boolean studentVisible;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", referencedColumnName = "id", nullable = false)
    private AchievementEntity achievementEntity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_subject_acronym", referencedColumnName = "subject_acronym", nullable = false)
    @JoinColumn(name = "game_course", referencedColumnName = "course", nullable = false)
    @JoinColumn(name = "game_period", referencedColumnName = "period", nullable = false)
    private GameEntity gameEntity;

}
