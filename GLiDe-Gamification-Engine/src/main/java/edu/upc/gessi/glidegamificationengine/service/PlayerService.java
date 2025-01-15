package edu.upc.gessi.glidegamificationengine.service;

import edu.upc.gessi.glidegamificationengine.dto.IndividualPlayerDTO;
import edu.upc.gessi.glidegamificationengine.dto.PlayerAchievementDTO;
import edu.upc.gessi.glidegamificationengine.dto.PlayerLoggedAchievementDTO;
import edu.upc.gessi.glidegamificationengine.dto.TeamPlayerDTO;

import java.util.List;

public interface PlayerService {

    TeamPlayerDTO getTeamPlayer(String teamPlayerPlayername);

    IndividualPlayerDTO getIndividualPlayer(String individualPlayerPlayername);

    List<PlayerAchievementDTO> getPlayerAchievements(String playerPlayername, Boolean achievementAttained, String achievementCategory);

    List<PlayerLoggedAchievementDTO> getPlayerLoggedAchievements(String playerPlayername, Boolean loggedAchievementViewed, String achievementCategory);

    PlayerLoggedAchievementDTO setPlayerLoggedAchievementViewed(String playerPlayername, Long loggedAchievementId, Boolean viewed);

}
