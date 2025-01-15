package edu.upc.gessi.glidegamificationengine.dto;

import edu.upc.gessi.glidegamificationengine.type.ActionCategoryType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import edu.upc.gessi.glidegamificationengine.type.SourceDataToolType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluableActionDTO {
    private String id;
    private String name;
    private String description;
    private ActionCategoryType category;
    private PlayerType assessmentLevel;
    private SourceDataToolType sourceDataTool;
    private String sourceDataAPIEndpoint;
}
