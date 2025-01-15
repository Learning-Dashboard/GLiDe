package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.type.ActionCategoryType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import edu.upc.gessi.glidegamificationengine.type.SourceDataToolType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluable_action")
public class EvaluableActionEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionCategoryType category;

    @Column(name = "assessment_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerType assessmentLevel;

    @Column(name = "source_data_tool", nullable = false)
    @Enumerated(EnumType.STRING)
    private SourceDataToolType sourceDataTool;

    @Column(name = "source_data_api_endpoint", nullable = false)
    private String sourceDataAPIEndpoint;

}
