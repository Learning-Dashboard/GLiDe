package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.LeaderboardDTO;
import edu.upc.gessi.glidegamificationengine.dto.LeaderboardResultDTO;
import edu.upc.gessi.glidegamificationengine.entity.AchievementEntity;
import edu.upc.gessi.glidegamificationengine.entity.GameEntity;
import edu.upc.gessi.glidegamificationengine.entity.LeaderboardEntity;
import edu.upc.gessi.glidegamificationengine.entity.GameGroupEntity;
import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.MissingInformationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.mapper.LeaderboardMapper;
import edu.upc.gessi.glidegamificationengine.repository.LeaderboardRepository;
import edu.upc.gessi.glidegamificationengine.service.LeaderboardService;
import edu.upc.gessi.glidegamificationengine.type.AnonymizationType;
import edu.upc.gessi.glidegamificationengine.type.ExtentType;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private AchievementServiceImpl achievementService;

    /* Methods callable from Service Layer */

    protected LeaderboardEntity getLeaderboardEntityById(Long leaderboardId) {
        return leaderboardRepository.findById(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException("Leaderboard with id '" + leaderboardId + "' not found."));
    }

    protected HashMap<String, List<LeaderboardResultDTO>> getLeaderboardResultsBySubject(LeaderboardEntity leaderboardEntity, HashMap<String, Integer> achievementUnitsResults, HashMap<String, Date> achievementDatesResults) {
        Map<String, String> playerImages;
        if (leaderboardEntity.getAssessmentLevel().equals(PlayerType.Team)) {
            playerImages = leaderboardEntity.getGameEntity().getTeamPlayerLogos();
        }
        else {
            playerImages = leaderboardEntity.getGameEntity().getIndividualPlayerAvatars();
        }

        List<String> orderedPlayersWithAchievementResults;
        if (leaderboardEntity.getAchievementEntity().getCategory().isNumerical()) {
            orderedPlayersWithAchievementResults = achievementUnitsResults
                    .entrySet()
                    .stream()
                    .sorted((Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> {
                        return o2.getValue().equals(o1.getValue()) ?
                                o1.getKey().compareTo(o2.getKey())
                                : o2.getValue().compareTo(o1.getValue());
                    })
                    .map(e -> e.getKey())
                    .toList();
        } else {
            orderedPlayersWithAchievementResults = achievementDatesResults
                    .entrySet()
                    .stream()
                    .sorted((Map.Entry<String, Date> o1, Map.Entry<String, Date> o2) -> {
                        return o1.getValue().equals(o2.getValue()) ?
                                o1.getKey().compareTo(o2.getKey())
                                : o1.getValue().compareTo(o2.getValue());
                    })
                    .map(e -> e.getKey())
                    .toList();
        }

        HashMap<String, List<LeaderboardResultDTO>> leaderboardResults = new HashMap<>();
        List<LeaderboardResultDTO> leaderboardResultDtos = new ArrayList<>();
        for (int i = 0; i < orderedPlayersWithAchievementResults.size(); i++) {
            LeaderboardResultDTO leaderboardResultDto = new LeaderboardResultDTO();
            leaderboardResultDto.setPosition(i + 1);
            leaderboardResultDto.setPlayername(orderedPlayersWithAchievementResults.get(i));
            leaderboardResultDto.setPlayerimage(playerImages.get(orderedPlayersWithAchievementResults.get(i)));
            leaderboardResultDto.setAchievementunits(achievementUnitsResults.get(orderedPlayersWithAchievementResults.get(i)));
            leaderboardResultDto.setAchievementdate(achievementDatesResults.get(orderedPlayersWithAchievementResults.get(i)));
            if (i != 0) {
                if (leaderboardEntity.getAchievementEntity().getCategory().isNumerical() && leaderboardResultDtos.get(i - 1).getAchievementunits().equals(leaderboardResultDto.getAchievementunits())) {
                    leaderboardResultDto.setPosition(leaderboardResultDtos.get(i - 1).getPosition());
                } else if (!leaderboardEntity.getAchievementEntity().getCategory().isNumerical() && leaderboardResultDtos.get(i - 1).getAchievementdate().equals(leaderboardResultDto.getAchievementdate())) {
                    leaderboardResultDto.setPosition(leaderboardResultDtos.get(i - 1).getPosition());
                }
            }
            leaderboardResultDtos.add(leaderboardResultDto);
            playerImages.remove(orderedPlayersWithAchievementResults.get(i));
        }
        List<String> orderedPlayersWithoutAchievementResults = playerImages
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(e -> e.getKey())
                .toList();
        for (int i = 0; i < orderedPlayersWithoutAchievementResults.size(); i++) {
            LeaderboardResultDTO leaderboardResultDto = new LeaderboardResultDTO();
            leaderboardResultDto.setPosition(0);
            leaderboardResultDto.setPlayername(orderedPlayersWithoutAchievementResults.get(i));
            leaderboardResultDto.setPlayerimage(playerImages.get(orderedPlayersWithoutAchievementResults.get(i)));
            leaderboardResultDto.setAchievementunits(0);
            leaderboardResultDto.setAchievementdate(null);
            leaderboardResultDtos.add(leaderboardResultDto);
        }
        leaderboardResults.put(leaderboardEntity.getGameEntity().getId().getSubjectAcronym() + " " + leaderboardEntity.getGameEntity().getId().getCourse().toString() + " " + leaderboardEntity.getGameEntity().getId().getPeriod().toString(), leaderboardResultDtos);
        return leaderboardResults;
    }

    protected HashMap<String, List<LeaderboardResultDTO>> getLeaderboardResultsByGroup(LeaderboardEntity leaderboardEntity, HashMap<String, Integer> achievementUnitsResults, HashMap<String, Date> achievementDatesResults) {
        HashMap<String, List<LeaderboardResultDTO>> leaderboardResults = new HashMap<>();

        for (GameGroupEntity gameGroupEntity : leaderboardEntity.getGameEntity().getGameGroupEntities()) {
            Map<String, String> playerImages;
            if (leaderboardEntity.getAssessmentLevel().equals(PlayerType.Team)) {
                playerImages = gameGroupEntity.getTeamPlayerLogos();
            }
            else {
                playerImages = gameGroupEntity.getIndividualPlayerAvatars();
            }

            HashMap<String, Integer> newAchievementUnitsResults = new HashMap<>(achievementUnitsResults);
            newAchievementUnitsResults.keySet().retainAll(playerImages.keySet());
            HashMap<String, Date> newAchievementDatesResults = new HashMap<>(achievementDatesResults);
            newAchievementDatesResults.keySet().retainAll(playerImages.keySet());

            List<String> orderedPlayersWithAchievementResults;
            if (leaderboardEntity.getAchievementEntity().getCategory().isNumerical()) {
                orderedPlayersWithAchievementResults = newAchievementUnitsResults
                        .entrySet()
                        .stream()
                        .sorted((Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> {
                            return o2.getValue().equals(o1.getValue()) ?
                                    o1.getKey().compareTo(o2.getKey())
                                    : o2.getValue().compareTo(o1.getValue());
                        })
                        .map(e -> e.getKey())
                        .toList();
            }
            else {
                orderedPlayersWithAchievementResults = newAchievementDatesResults
                        .entrySet()
                        .stream()
                        .sorted((Map.Entry<String, Date> o1, Map.Entry<String, Date> o2) -> {
                            return o1.getValue().equals(o2.getValue()) ?
                                    o1.getKey().compareTo(o2.getKey())
                                    : o1.getValue().compareTo(o2.getValue());
                        })
                        .map(e -> e.getKey())
                        .toList();
            }

            List<LeaderboardResultDTO> leaderboardResultDtos = new ArrayList<>();
            for (int i = 0; i < orderedPlayersWithAchievementResults.size(); i++) {
                LeaderboardResultDTO leaderboardResultDto = new LeaderboardResultDTO();
                leaderboardResultDto.setPosition(i + 1);
                leaderboardResultDto.setPlayername(orderedPlayersWithAchievementResults.get(i));
                leaderboardResultDto.setPlayerimage(playerImages.get(orderedPlayersWithAchievementResults.get(i)));
                leaderboardResultDto.setAchievementunits(newAchievementUnitsResults.get(orderedPlayersWithAchievementResults.get(i)));
                leaderboardResultDto.setAchievementdate(newAchievementDatesResults.get(orderedPlayersWithAchievementResults.get(i)));
                if (i != 0) {
                    if (leaderboardEntity.getAchievementEntity().getCategory().isNumerical() && leaderboardResultDtos.get(i - 1).getAchievementunits().equals(leaderboardResultDto.getAchievementunits())) {
                        leaderboardResultDto.setPosition(leaderboardResultDtos.get(i - 1).getPosition());
                    } else if (!leaderboardEntity.getAchievementEntity().getCategory().isNumerical() && leaderboardResultDtos.get(i - 1).getAchievementdate().equals(leaderboardResultDto.getAchievementdate())) {
                        leaderboardResultDto.setPosition(leaderboardResultDtos.get(i - 1).getPosition());
                    }
                }
                leaderboardResultDtos.add(leaderboardResultDto);
                playerImages.remove(orderedPlayersWithAchievementResults.get(i));
            }
            List<String> orderedPlayersWithoutAchievementResults = playerImages
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(e -> e.getKey())
                    .toList();
            for (int i = 0; i < orderedPlayersWithoutAchievementResults.size(); i++) {
                LeaderboardResultDTO leaderboardResultDto = new LeaderboardResultDTO();
                leaderboardResultDto.setPosition(0);
                leaderboardResultDto.setPlayername(orderedPlayersWithoutAchievementResults.get(i));
                leaderboardResultDto.setPlayerimage(playerImages.get(orderedPlayersWithoutAchievementResults.get(i)));
                leaderboardResultDto.setAchievementunits(0);
                leaderboardResultDto.setAchievementdate(null);
                leaderboardResultDtos.add(leaderboardResultDto);
            }
            leaderboardResults.put(leaderboardEntity.getGameEntity().getId().getSubjectAcronym() + gameGroupEntity.getId().getGroup() + " " + leaderboardEntity.getGameEntity().getId().getCourse().toString() + " " + leaderboardEntity.getGameEntity().getId().getPeriod().toString(), leaderboardResultDtos);
        }

        return leaderboardResults;
    }


    /* Methods callable from Controller Layer */

    @Override
    public LeaderboardDTO createLeaderboard(String leaderboardName, Date leaderboardStartDate, Date leaderboardEndDate, String leaderboardAssessmentLevel, String leaderboardExtent, String leaderboardAnonymization, Boolean leaderboardStudentVisible, Long achievementId, String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        PlayerType leaderboardAssessmentLevelType = PlayerType.fromString(leaderboardAssessmentLevel);
        ExtentType leaderboardExtentType = ExtentType.fromString(leaderboardExtent);
        AnonymizationType leaderboardAnonymizationType = AnonymizationType.fromString(leaderboardAnonymization);
        PeriodType gamePeriodType = PeriodType.fromString(gamePeriod);

        AchievementEntity achievementEntity = achievementService.getAchievementEntityById(achievementId);
        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setCourse(gameCourse);
        gameKey.setPeriod(gamePeriodType);
        GameEntity gameEntity = gameService.getGameEntityByKey(gameKey);

        if (leaderboardName.isBlank())
            throw new ConstraintViolationException("Leaderboard name cannot be blank, please introduce a name.");
        if (leaderboardStartDate.after(leaderboardEndDate))
            throw new ConstraintViolationException("Leaderboard start date cannot be posterior to leaderboard end date, please introduce different dates.");
        if (!gameEntity.hasAchievement(achievementEntity))
            throw new ConstraintViolationException("The game assigned to the leaderboard must have at least one rule with the same achievement assigned to the leaderboard.");

        LeaderboardEntity leaderboardEntity = new LeaderboardEntity();
        leaderboardEntity.setName(leaderboardName);
        leaderboardEntity.setStartDate(leaderboardStartDate);
        leaderboardEntity.setEndDate(leaderboardEndDate);
        leaderboardEntity.setAssessmentLevel(leaderboardAssessmentLevelType);
        leaderboardEntity.setExtent(leaderboardExtentType);
        leaderboardEntity.setAnonymization(leaderboardAnonymizationType);
        leaderboardEntity.setStudentVisible(leaderboardStudentVisible);
        leaderboardEntity.setAchievementEntity(achievementEntity);
        achievementEntity.addLeaderboardEntity(leaderboardEntity);
        leaderboardEntity.setGameEntity(gameEntity);
        LeaderboardEntity savedLeaderboardEntity = leaderboardRepository.save(leaderboardEntity);
        return LeaderboardMapper.mapToLeaderboardDto(savedLeaderboardEntity);
    }

    @Override
    public List<LeaderboardDTO> getLeaderboards(String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        List<LeaderboardEntity> leaderboardEntities;

        if (gameSubjectAcronym != null || gameCourse != null || gamePeriod != null) {
            if (gameSubjectAcronym == null || gameCourse == null || gamePeriod == null) {
                List<String> missingInformation = new ArrayList<>();
                if (gameSubjectAcronym == null) missingInformation.add("subject acronym");
                if (gameCourse == null) missingInformation.add("course");
                if (gamePeriod == null) missingInformation.add("period");
                throw new MissingInformationException("Game not fully identified (Missing: " +
                        missingInformation.stream().map(value -> "'" + value + "'").collect(Collectors.joining(", ")) + ").");
            }
            PeriodType gamePeriodType = PeriodType.fromString(gamePeriod);
            GameKey gameKey = new GameKey();
            gameKey.setSubjectAcronym(gameSubjectAcronym);
            gameKey.setCourse(gameCourse);
            gameKey.setPeriod(gamePeriodType);
            leaderboardEntities = leaderboardRepository.findByGameEntity(gameService.getGameEntityByKey(gameKey));
        }
        else {
            leaderboardEntities = leaderboardRepository.findAll();
        }

        return leaderboardEntities.stream().map((leaderboardEntity -> LeaderboardMapper.mapToLeaderboardDto(leaderboardEntity)))
                .collect(Collectors.toList());
    }

    @Override
    public LeaderboardDTO getLeaderboard(Long leaderboardId) {
        return LeaderboardMapper.mapToLeaderboardDto(getLeaderboardEntityById(leaderboardId));
    }

    @Override
    public void deleteLeaderboard(Long leaderboardId){
        LeaderboardEntity leaderboardEntity = getLeaderboardEntityById(leaderboardId);

        leaderboardRepository.deleteById(leaderboardEntity.getId());
    }

    @Override
    public LeaderboardDTO updateLeaderboard(Long leaderboardId, String leaderboardName, Date leaderboardStartDate, Date leaderboardEndDate, String leaderboardAssessmentLevel, String leaderboardExtent, String leaderboardAnonymization, Boolean leaderboardStudentVisible, Long achievementId, String gameSubjectAcronym, Integer gameCourse, String gamePeriod){
        LeaderboardEntity leaderboardEntity = getLeaderboardEntityById(leaderboardId);
        if (leaderboardName.isBlank() || leaderboardStartDate.after(leaderboardEndDate))
            throw new ConstraintViolationException("Invalid parameters entered.");
        leaderboardEntity.setName(leaderboardName);
        leaderboardEntity.setStartDate(leaderboardStartDate);
        leaderboardEntity.setEndDate(leaderboardEndDate);
        leaderboardEntity.setAssessmentLevel(PlayerType.fromString(leaderboardAssessmentLevel));
        leaderboardEntity.setExtent(ExtentType.fromString(leaderboardExtent));
        leaderboardEntity.setAnonymization(AnonymizationType.fromString(leaderboardAnonymization));
        leaderboardEntity.setStudentVisible(leaderboardStudentVisible);

        AchievementEntity achievementEntity = achievementService.getAchievementEntityById(achievementId);
        leaderboardEntity.setAchievementEntity(achievementEntity);

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setCourse(gameCourse);
        gameKey.setPeriod(PeriodType.fromString(gamePeriod));
        GameEntity gameEntity = gameService.getGameEntityByKey(gameKey);
        leaderboardEntity.setGameEntity(gameEntity);

        LeaderboardEntity updatedLeaderboardEntity = leaderboardRepository.save(leaderboardEntity);

        return LeaderboardMapper.mapToLeaderboardDto(updatedLeaderboardEntity);
    }

    @Override
    @Transactional
    public HashMap<String, List<LeaderboardResultDTO>> getLeaderboardResults(Long leaderboardId) {
        LeaderboardEntity leaderboardEntity = getLeaderboardEntityById(leaderboardId);

        GameKey gameId = leaderboardEntity.getGameEntity().getId();
        List<Map> achievementResults = leaderboardEntity.getAchievementEntity().getResults(gameId, leaderboardEntity.getAssessmentLevel(), leaderboardEntity.getStartDate(), leaderboardEntity.getEndDate());
        HashMap<String, Integer> achievementUnitsResults = (HashMap<String, Integer>) achievementResults.getFirst();
        HashMap<String, Date> achievementDatesResults = (HashMap<String, Date>) achievementResults.get(1);

        if (leaderboardEntity.getExtent().equals(ExtentType.Subject))
            return getLeaderboardResultsBySubject(leaderboardEntity, achievementUnitsResults, achievementDatesResults);
        else
            return getLeaderboardResultsByGroup(leaderboardEntity, achievementUnitsResults, achievementDatesResults);
    }

}
