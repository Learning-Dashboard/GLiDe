package edu.upc.gessi.glidegamificationengine.dto;

import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlayerDTO {
    private String playername;
    private Integer points;
    private Integer level;
    private PlayerType type;
    private String logo;
}
