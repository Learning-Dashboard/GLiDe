package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.type.RuleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "simple_rule")
@Check(constraints = "repetitions >= 1")
public class SimpleRuleEntity extends RuleEntity {

    @Column(name = "repetitions", nullable = false)
    private Integer repetitions;

    public SimpleRuleEntity(Long id, String name, GameEntity gameEntity, EvaluableActionEntity evaluableActionEntity, Integer repetitions) {
        super(id, name, RuleType.Simple, null, gameEntity, evaluableActionEntity);
        this.repetitions = repetitions;
    }

    @Override
    public Boolean checkRuleValidity(Date currentDate) {
        return true;
    }

    @Override
    public Boolean evaluateRule(String evaluableActionId, String playerPlayername) {
        Boolean loggedAchievementEntitiesRequired = false;

        Integer loggedActionEntitiesNumber = super.getAchievementAssignmentEntity().getLoggedActionEntitiesNumber(evaluableActionId, playerPlayername);
        if (loggedActionEntitiesNumber%repetitions == 0) {
            if (super.getAchievementAssignmentEntity().getOnlyFirstTime()) {
                loggedAchievementEntitiesRequired = (loggedActionEntitiesNumber.equals(repetitions));
            }
            else {
                loggedAchievementEntitiesRequired = true;
            }
        }

        return loggedAchievementEntitiesRequired;
    }

}
