package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.entity.key.AchievementAssignmentKey;
import edu.upc.gessi.glidegamificationengine.type.ActionCategoryType;
import edu.upc.gessi.glidegamificationengine.type.ConditionType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "achievement_assignment")
@Check(constraints = "achievement_units >= 1")
public class AchievementAssignmentEntity {

    @EmbeddedId
    private AchievementAssignmentKey id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId("ruleId")
    @JoinColumn(name = "rule_id", referencedColumnName = "id")
    private RuleEntity ruleEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("achievementId")
    @JoinColumn(name = "achievement_id")
    private AchievementEntity achievementEntity;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "only_first_time", nullable = false)
    private Boolean onlyFirstTime;

    @Column(name = "condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConditionType condition;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="achievement_assignment_condition_parameters", joinColumns = {@JoinColumn(name = "achievement_assignment_rule_id", referencedColumnName = "rule_id"),
                                                                                        @JoinColumn(name = "achievement_assignment_achievement_id", referencedColumnName = "achievement_id")})
    private List<Float> conditionParameters;

    @Column(name = "achievement_units", nullable = false)
    private Integer achievementUnits;

    @Column(name = "assessment_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerType assessmentLevel;

    @OneToMany(mappedBy = "achievementAssignmentEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LoggedAchievementEntity> loggedAchievementEntities;

    @ManyToMany
    @JoinTable(name = "achievement_assignment_accomplished_logged_action",
            joinColumns = {@JoinColumn(name = "achievement_assignment_rule_id", referencedColumnName = "rule_id"),
                           @JoinColumn(name = "achievement_assignment_achievement_id", referencedColumnName = "achievement_id")},
            inverseJoinColumns = {@JoinColumn(name = "logged_action_evaluable_action_id", referencedColumnName = "evaluable_action_id"),
                                  @JoinColumn(name = "logged_action_player_playername", referencedColumnName = "player_playername"),
                                  @JoinColumn(name = "logged_action_timestamp", referencedColumnName = "timestamp")})
    private List<LoggedActionEntity> loggedActionEntities;

    public void addLoggedAchievementEntity(LoggedAchievementEntity loggedAchievementEntity) {
        if (loggedAchievementEntities == null) {
            loggedAchievementEntities = new ArrayList<>();
        }
        loggedAchievementEntities.add(loggedAchievementEntity);
    }

    public void addLoggedActionEntity(LoggedActionEntity loggedActionEntity) {
        if (loggedActionEntities == null) {
            loggedActionEntities = new ArrayList<>();
        }
        loggedActionEntities.add(loggedActionEntity);
    }

    public Boolean evaluateLoggedActionEntity(LoggedActionEntity loggedActionEntity) {
        Boolean loggedActionEntityAdded = false;

        if (loggedActionEntity.getType().equals(ActionCategoryType.PlayerPerformance)) {
            PlayerPerformanceLoggedActionEntity playerPerformanceLoggedActionEntity = (PlayerPerformanceLoggedActionEntity) loggedActionEntity;
            Float value = playerPerformanceLoggedActionEntity.getValue();
            Boolean conditionMet = condition.checkCondition(value, conditionParameters);
            if (conditionMet) {
                addLoggedActionEntity(loggedActionEntity);
                loggedActionEntityAdded = true;
            }
        }

        return loggedActionEntityAdded;
    }

    public Integer getLoggedActionEntitiesNumber(String evaluableActionId, String playerPlayername) {
        Integer loggedActionEntitiesNumber = 0;

        for (LoggedActionEntity loggedActionEntity : loggedActionEntities) {
            if (loggedActionEntity.getEvaluableActionEntity().getId().equals(evaluableActionId) && loggedActionEntity.getPlayerEntity().getPlayername().equals(playerPlayername)) {
                loggedActionEntitiesNumber++;
            }
        }

        return loggedActionEntitiesNumber;
    }

    public Integer getLoggedActionEntitiesNumber(String evaluableActionId, String playerPlayername, Date startDate, Date endDate) {
        Integer loggedActionEntitiesNumber = 0;

        for (LoggedActionEntity loggedActionEntity : loggedActionEntities) {
            if (loggedActionEntity.getEvaluableActionEntity().getId().equals(evaluableActionId) && loggedActionEntity.getPlayerEntity().getPlayername().equals(playerPlayername)) {
                Timestamp timestamp = loggedActionEntity.getId().getTimestamp();
                Date timestampDate = Date.valueOf(timestamp.toLocalDateTime().toLocalDate());
                if (!timestampDate.before(startDate) && !timestampDate.after(endDate)) {
                    loggedActionEntitiesNumber++;
                }
            }
        }

        return loggedActionEntitiesNumber;
    }

}
