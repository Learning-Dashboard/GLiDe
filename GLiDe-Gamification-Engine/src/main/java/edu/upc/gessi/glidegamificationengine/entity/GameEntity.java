package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.type.StateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game")
@Check(constraints = "start_date <= end_date")
public class GameEntity {

    @EmbeddedId
    private GameKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subjectAcronym")
    @JoinColumn(name = "subject_acronym")
    private SubjectEntity subjectEntity;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="game_level_policy_function_parameters", joinColumns = {@JoinColumn(name = "game_subject_acronym", referencedColumnName = "subject_acronym"),
                                                                                  @JoinColumn(name = "game_course", referencedColumnName = "course"),
                                                                                  @JoinColumn(name = "game_period", referencedColumnName = "period")})
    private List<Float> levelPolicyFunctionParameters;

    @OneToMany(mappedBy = "gameEntity", fetch = FetchType.LAZY)
    private List<GameGroupEntity> gameGroupEntities;

    @OneToMany(mappedBy = "gameEntity", fetch = FetchType.LAZY)
    private List<RuleEntity> ruleEntities;

    public StateType getState() {
        return StateType.getState(startDate, endDate);
    }

    public void addRuleEntity(RuleEntity ruleEntity) {
        if (ruleEntities == null) {
            ruleEntities = new ArrayList<>();
        }
        ruleEntities.add(ruleEntity);
    }

    public Map<String, String> getTeamPlayerLogos() {
        Map<String, String> teamPlayerLogos = new HashMap<>();
        for (GameGroupEntity gameGroupEntity : gameGroupEntities) {
            for (ProjectEntity projectEntity : gameGroupEntity.getProjectEntities()) {
                teamPlayerLogos.put(projectEntity.getTeamPlayerEntity().getPlayername(), projectEntity.getTeamPlayerEntity().getLogo());
            }
        }
        return teamPlayerLogos;
    }

    public Map<String, String> getIndividualPlayerAvatars() {
        Map<String, String> individualPlayerAvatars = new HashMap<>();
        for (GameGroupEntity gameGroupEntity : gameGroupEntities) {
            for (ProjectEntity projectEntity : gameGroupEntity.getProjectEntities()) {
                for (IndividualPlayerEntity individualPlayerEntity : projectEntity.getTeamPlayerEntity().getIndividualPlayerEntities()) {
                    individualPlayerAvatars.put(individualPlayerEntity.getPlayername(), individualPlayerEntity.getAvatar());
                }
            }
        }
        return individualPlayerAvatars;
    }

    public Integer calculateLevel(Integer points) {
        Integer level = 1;
        while (true) {
            Integer necessaryPoints = (int) (levelPolicyFunctionParameters.getFirst() * Math.pow(levelPolicyFunctionParameters.get(1), (level * levelPolicyFunctionParameters.getLast())));
            if (necessaryPoints > points) return level-1;
            level++;
        }
    }

    public Boolean hasAchievement(AchievementEntity achievementEntity) {
        for (RuleEntity ruleEntity : ruleEntities) {
            if (ruleEntity.getAchievementAssignmentEntity().getAchievementEntity().equals(achievementEntity)) return true;
        }
        return false;
    }

}
