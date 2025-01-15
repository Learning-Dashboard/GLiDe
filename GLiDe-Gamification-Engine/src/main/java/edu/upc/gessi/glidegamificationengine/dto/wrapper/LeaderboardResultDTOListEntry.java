package edu.upc.gessi.glidegamificationengine.dto.wrapper;

import edu.upc.gessi.glidegamificationengine.dto.LeaderboardResultDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardResultDTOListEntry {
    private String leaderboardExtentIdentifier;
    private List<LeaderboardResultDTO> leaderboardResults;
}
