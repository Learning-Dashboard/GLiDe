package edu.upc.gessi.glidebackend.service;

import java.util.List;

public interface LeaderboardService {

    List<Object> getLeaderboards(String gameSubjectAcronym, Integer gameCourse, String gamePeriod);

    Object getLeaderboard(Long leaderboardId);

    List<Object> getLeaderboardResults(Long leaderboardId);

}
