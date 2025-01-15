package edu.upc.gessi.glidegamificationengine.dto;

import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import edu.upc.gessi.glidegamificationengine.type.StateType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
        private String subjectAcronym;
        private Integer course;
        private PeriodType period;

        @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd")
        private Date startDate;

        @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd")
        private Date endDate;

        private List<Float> levelPolicyFunctionParameters;
        private StateType state;
}
