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
public class GameKey implements Serializable {

    @Column(name = "subject_acronym")
    private String subjectAcronym;

    @Column(name = "course")
    private Integer course;

    @Column(name = "period")
    @Enumerated(EnumType.STRING)
    private PeriodType period;

}
