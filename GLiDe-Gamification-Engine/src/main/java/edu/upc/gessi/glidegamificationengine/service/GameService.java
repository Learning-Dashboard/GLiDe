package edu.upc.gessi.glidegamificationengine.service;

import edu.upc.gessi.glidegamificationengine.dto.GameDTO;

import java.sql.Date;
import java.util.List;
import java.time.LocalDate;

public interface GameService {

    List<GameDTO> getGames(String gameSubjectAcronym, Integer gameCourse, String gamePeriod);

    void evaluateGame(String gameSubjectAcronym, Integer gameCourse, String gamePeriod, LocalDate evaluationDate);

    GameDTO createGame(String subjectAcronym, Integer course, String period, Date startDate, Date endDate, Float firstLevelPolicyParameter, Float secondLevelPolicyParameter, Float thirdLevelPolicyParameter);

    GameDTO updateGame(String subjectAcronym, Integer course, String period, Date startDate, Date endDate);
}
