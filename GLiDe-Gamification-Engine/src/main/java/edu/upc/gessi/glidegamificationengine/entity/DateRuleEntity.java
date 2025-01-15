package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.type.RuleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "date_rule")
@Check(constraints = "repetitions >= 1 AND start_date <= end_date")
public class DateRuleEntity extends RuleEntity {

    @Column(name = "repetitions", nullable = false)
    private Integer repetitions;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    public DateRuleEntity(Long id, String name, GameEntity gameEntity, EvaluableActionEntity evaluableActionEntity, Integer repetitions, Date startDate, Date endDate) {
        super(id, name, RuleType.Date, null, gameEntity, evaluableActionEntity);
        this.repetitions = repetitions;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Boolean checkRuleValidity(Date currentDate) {
        if (currentDate.before(startDate) || currentDate.after(endDate)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Boolean evaluateRule(String evaluableActionId, String playerPlayername) {
        Boolean loggedAchievementEntitiesRequired = false;

        Integer loggedActionEntitiesNumber = super.getAchievementAssignmentEntity().getLoggedActionEntitiesNumber(evaluableActionId, playerPlayername, startDate, endDate);
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
