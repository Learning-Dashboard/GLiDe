package edu.upc.gessi.glidegamificationengine.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class AchievementAssignmentKey implements Serializable {

    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "achievement_id")
    private Long achievementId;

}
