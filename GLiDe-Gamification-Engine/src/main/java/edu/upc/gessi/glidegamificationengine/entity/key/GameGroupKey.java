package edu.upc.gessi.glidegamificationengine.entity.key;

import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class GameGroupKey implements Serializable {

    @Column(name = "game_subject_acronym")
    private String gameSubjectAcronym;

    @Column(name = "game_course")
    private Integer gameCourse;

    @Column(name = "game_period")
    @Enumerated(EnumType.STRING)
    private PeriodType gamePeriod;

    @Column(name = "`group`")
    private Integer group;

}
