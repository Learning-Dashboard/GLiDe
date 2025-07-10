package edu.upc.gessi.glidebackend.service;

import java.util.List;

public interface GamificationService {

    Object getIndividualPlayer(String individualPlayerPlayername);

    Object getTeamPlayer(String teamPlayerPlayername);

    List<Object> getPlayerAchievements(String teamPlayerPlayername, String attained, String category);

    List<Object> getEvaluableActions();
}
