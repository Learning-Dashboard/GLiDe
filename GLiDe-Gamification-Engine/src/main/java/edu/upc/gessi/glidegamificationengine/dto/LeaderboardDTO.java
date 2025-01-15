package edu.upc.gessi.glidegamificationengine.dto;

import edu.upc.gessi.glidegamificationengine.type.AnonymizationType;
import edu.upc.gessi.glidegamificationengine.type.ExtentType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardDTO {
    private Long id;
    private String name;

    @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd")
    private Date startDate;

    @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd")
    private Date endDate;

    private PlayerType assessmentLevel;
    private ExtentType extent;
    private AnonymizationType anonymization;
    private Boolean studentVisible;
    private Long achievementId;
}
