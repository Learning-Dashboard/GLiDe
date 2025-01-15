package edu.upc.gessi.glidebackend.dto;

import edu.upc.gessi.glidebackend.type.PlayerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndividualPlayerDto {
    private String playername;
    private Integer points;
    private Integer level;
    private PlayerType type;
    private String role;
    private String teamPlayername;
    private String project;
    private String learningdashboardUsername;
    private String githubUsername;
    private String taigaUsername;
}
