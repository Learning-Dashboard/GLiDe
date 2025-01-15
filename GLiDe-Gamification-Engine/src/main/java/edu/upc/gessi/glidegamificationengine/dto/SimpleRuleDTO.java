package edu.upc.gessi.glidegamificationengine.dto;

import edu.upc.gessi.glidegamificationengine.type.ConditionType;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import edu.upc.gessi.glidegamificationengine.type.RuleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleRuleDTO {
    private Long id;
    private String name;
    private RuleType type;
    private Integer repetitions;
    private String gameSubjectAcronym;
    private Integer gameCourse;
    private PeriodType gamePeriod;
    private String evaluableActionId;
    private Long achievementId;
    private String achievementAssignmentMessage;
    private Boolean achievementAssignmentOnlyFirstTime;
    private ConditionType achievementAssignmentCondition;
    private List<Float> achievementAssignmentConditionParameters;
    private Integer achievementAssignmentUnits;
    private PlayerType achievementAssignmentAssessmentLevel;
}
