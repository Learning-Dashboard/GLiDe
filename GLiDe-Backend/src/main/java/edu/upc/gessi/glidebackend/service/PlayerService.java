package edu.upc.gessi.glidebackend.service;

import edu.upc.gessi.glidebackend.dto.*;

import java.util.List;

public interface PlayerService {

    IndividualPlayerDto getIndividualPlayer(String individualPlayerPlayername);

    PlayerMonitoringDto getPlayerMonitoring(String individualPlayerPlayername);

    PlayerMonitoringDto setPlayerMonitoring(String playerPlayername, String selectedMetrics, String selectedHistoryMetrics, String selectedBarMetrics);

    PlayerMonitoringDto setPlayerMonitoringDates(String playerPlayername, String startDate, String endDate);

    PlayerGamificationDto getPlayerGamification(String individualPlayerPlayername);

    List<IndividualPlayerDto> getAllIndividualPlayers();

    StudentUserDto getStudentUser(String individualPlayerPlayername);
}
