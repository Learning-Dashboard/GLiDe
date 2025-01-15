package edu.upc.gessi.glidegamificationengine.service;

import edu.upc.gessi.glidegamificationengine.dto.LeaderboardDTO;
import edu.upc.gessi.glidegamificationengine.dto.LeaderboardResultDTO;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public interface LeaderboardService {

    LeaderboardDTO createLeaderboard(String leaderboardName, Date leaderboardStartDate, Date leaderboardEndDate, String leaderboardAssessmentLevel, String leaderboardExtent, String leaderboardAnonymization, Boolean leaderboardStudentVisible, Long achievementId, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);

    List<LeaderboardDTO> getLeaderboards(String gameSubjectAcronym, Integer gameCourse, String gamePeriod);

    LeaderboardDTO getLeaderboard(Long leaderboardId);

    void deleteLeaderboard(Long leaderboardId);

    LeaderboardDTO updateLeaderboard(Long leaderboardId, String leaderboardName, Date leaderboardStartDate, Date leaderboardEndDate, String leaderboardAssessmentLevel, String leaderboardExtent, String leaderboardAnonymization, Boolean leaderboardStudentVisible, Long achievementId, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);

    HashMap<String, List<LeaderboardResultDTO>> getLeaderboardResults(Long leaderboardId);

}
