package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.type.AchievementCategoryType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "achievement")
public class AchievementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Lob
    @Column(name = "icon")
    private String icon;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private AchievementCategoryType category;

    @OneToMany(mappedBy = "achievementEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AchievementAssignmentEntity> achievementAssignmentEntities;

    @OneToMany(mappedBy = "achievementEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LeaderboardEntity> leaderboardEntities;

    public void addLeaderboardEntity(LeaderboardEntity leaderboardEntity) {
        if (leaderboardEntities == null) {
            leaderboardEntities = new ArrayList<>();
        }
        leaderboardEntities.add(leaderboardEntity);
    }

    public void addAchievementAssignmentEntity(AchievementAssignmentEntity achievementAssignmentEntity) {
        if (achievementAssignmentEntities == null) {
            achievementAssignmentEntities = new ArrayList<>();
        }
        achievementAssignmentEntities.add(achievementAssignmentEntity);
    }

    public List<Map> getResults(GameKey gameId, PlayerType assessmentLevel, Date startDate, Date endDate) {
        Map<String, Integer> playerUnits = new HashMap<>();
        Map<String, Date> playerDates = new HashMap<>();

        for (AchievementAssignmentEntity achievementAssignmentEntity : achievementAssignmentEntities) {
            if (achievementAssignmentEntity.getRuleEntity().getGameEntity().getId().equals(gameId) && achievementAssignmentEntity.getAssessmentLevel().equals(assessmentLevel)) {
                for (LoggedAchievementEntity loggedAchievementEntity : achievementAssignmentEntity.getLoggedAchievementEntities()) {
                    if (!(loggedAchievementEntity.getDate().before(startDate) || loggedAchievementEntity.getDate().after(endDate))) {
                        String playername = loggedAchievementEntity.getPlayerEntity().getPlayername();
                        if (playerUnits.containsKey(playername)) { //playerDates.containsKey(playername)
                            if (category.isNumerical()) {
                                playerUnits.put(playername, playerUnits.get(playername) + achievementAssignmentEntity.getAchievementUnits());
                                if (playerDates.get(playername).before(loggedAchievementEntity.getDate())) {
                                    playerDates.put(playername, loggedAchievementEntity.getDate());
                                }
                            } else {
                                playerUnits.put(playername, playerUnits.get(playername) + 1);
                                if (playerDates.get(playername).after(loggedAchievementEntity.getDate())) {
                                    playerDates.put(playername, loggedAchievementEntity.getDate());
                                }
                            }
                        } else {
                            if (category.isNumerical()) {
                                playerUnits.put(playername, achievementAssignmentEntity.getAchievementUnits());
                            } else {
                                playerUnits.put(playername, 1);
                            }
                            playerDates.put(playername, loggedAchievementEntity.getDate());
                        }
                    }
                }
            }
        }

        List<Map> results = new ArrayList<>();
        results.add(playerUnits);
        results.add(playerDates);
        return results;
    }
}
