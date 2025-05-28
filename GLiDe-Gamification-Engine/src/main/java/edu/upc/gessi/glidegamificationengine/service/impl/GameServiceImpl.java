package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.GameDTO;
import edu.upc.gessi.glidegamificationengine.entity.*;
import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.mapper.GameMapper;
import edu.upc.gessi.glidegamificationengine.repository.GameRepository;
import edu.upc.gessi.glidegamificationengine.repository.SubjectRepository;
import edu.upc.gessi.glidegamificationengine.service.GameService;
import edu.upc.gessi.glidegamificationengine.type.AchievementCategoryType;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import edu.upc.gessi.glidegamificationengine.type.StateType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private LoggedActionServiceImpl loggedActionService;

    @Autowired
    private LoggedAchievementServiceImpl loggedAchievementService;
    @Autowired
    private SubjectRepository subjectRepository;

    /* Private methods */

    private void evaluateRule(RuleEntity ruleEntity, PlayerEntity playerEntity, Timestamp timestamp) {
        logger.info("→ Iniciant evaluateRule per player: {}, acció: {}", playerEntity.getPlayername(), ruleEntity.getEvaluableActionEntity().getId());
        try {
            LoggedActionEntity loggedActionEntity = loggedActionService.getOrCreateLoggedActionEntityByKeyEntities(ruleEntity.getEvaluableActionEntity(), playerEntity, timestamp);
            logger.debug("LoggedAction obtingut: {}", loggedActionEntity.getId());
            Boolean loggedActionEntityAdded = ruleEntity.getAchievementAssignmentEntity().evaluateLoggedActionEntity(loggedActionEntity);
            logger.debug("LoggedActionEntity afegit? {}", loggedActionEntityAdded);
            if (loggedActionEntityAdded) {
                Boolean loggedAchievementEntitiesRequired = ruleEntity.evaluateRule(loggedActionEntity.getEvaluableActionEntity().getId(), loggedActionEntity.getPlayerEntity().getPlayername());
                logger.debug("Cal afegir achievement? {}", loggedAchievementEntitiesRequired);
                if (loggedAchievementEntitiesRequired) {
                    logger.info("Assignant achievement per jugador {}", playerEntity.getPlayername());
                    logger.info("RuleEntity: {}", ruleEntity.getId());
                    logger.info("AchievementAssignmentEntity: {}", ruleEntity.getAchievementAssignmentEntity().getId());
                    logger.info("AchievementEntity: {}", ruleEntity.getAchievementAssignmentEntity().getAchievementEntity().getId());
                    if (playerEntity.getType().equals(PlayerType.Team)) {
                        if (ruleEntity.getAchievementAssignmentEntity().getAssessmentLevel().equals(PlayerType.Team)) {
                            
                            loggedAchievementService.createLoggedAchievementEntity(Date.valueOf(timestamp.toLocalDateTime().toLocalDate()), ruleEntity.getAchievementAssignmentEntity(), playerEntity);
                        }
                        else { //PlayerType.Individual
                            TeamPlayerEntity teamPlayerEntity = (TeamPlayerEntity) playerEntity;
                            for (IndividualPlayerEntity individualPlayerEntity : teamPlayerEntity.getIndividualPlayerEntities()) {
                                loggedAchievementService.createLoggedAchievementEntity(Date.valueOf(timestamp.toLocalDateTime().toLocalDate()), ruleEntity.getAchievementAssignmentEntity(), individualPlayerEntity);
                            }
                        }
                    }
                    else { //PlayerType.Individual
                        if (ruleEntity.getAchievementAssignmentEntity().getAssessmentLevel().equals(PlayerType.Individual)) {
                            loggedAchievementService.createLoggedAchievementEntity(Date.valueOf(timestamp.toLocalDateTime().toLocalDate()), ruleEntity.getAchievementAssignmentEntity(), playerEntity);
                        }
                        else { //PlayerType.Team
                            IndividualPlayerEntity individualPlayerEntity = (IndividualPlayerEntity) playerEntity;
                            loggedAchievementService.createLoggedAchievementEntity(Date.valueOf(timestamp.toLocalDateTime().toLocalDate()), ruleEntity.getAchievementAssignmentEntity(), individualPlayerEntity.getTeamPlayerEntity());
                        }
                    }
                    if (ruleEntity.getAchievementAssignmentEntity().getAchievementEntity().getCategory().equals(AchievementCategoryType.Points)) {
                        playerEntity.setPoints(playerEntity.getPoints() + ruleEntity.getAchievementAssignmentEntity().getAchievementUnits());
                        playerEntity.setLevel(ruleEntity.getGameEntity().calculateLevel(playerEntity.getPoints()));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("❌ Error dins evaluateRule per player: {} acció: {} - {}", playerEntity.getPlayername(), ruleEntity.getEvaluableActionEntity().getId(), e.getMessage(), e);
            throw e; 
        }
    }


    /* Methods callable from Service Layer */

    protected GameEntity getGameEntityByKey(GameKey gameKey) {
        return gameRepository.findById(gameKey)
                .orElseThrow(() -> new ResourceNotFoundException("Game with subject acronym '" + gameKey.getSubjectAcronym() + "', course '" + gameKey.getCourse() + "' and period '" + gameKey.getPeriod() + "' not found."));
    }


    /* Methods callable from Controller Layer */

    @Override
    public List<GameDTO> getGames(String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        List<GameEntity> gameEntities = new ArrayList<>();

        if (gameSubjectAcronym != null) {
            if (gameCourse != null) {
                if (gamePeriod != null) {
                    GameKey gameKey = new GameKey();
                    gameKey.setSubjectAcronym(gameSubjectAcronym);
                    gameKey.setCourse(gameCourse);
                    gameKey.setPeriod(PeriodType.fromString(gamePeriod));
                    Optional<GameEntity> gameEntity = gameRepository.findById(gameKey);
                    if (gameEntity.isPresent()) gameEntities.add(gameEntity.get());
                }
                else {
                    gameEntities = gameRepository.findByIdSubjectAcronymAndIdCourse(gameSubjectAcronym, gameCourse);
                }
            }
            else {
                if (gamePeriod != null) {
                    gameEntities = gameRepository.findByIdSubjectAcronymAndIdPeriod(gameSubjectAcronym, PeriodType.fromString(gamePeriod));
                }
                else {
                    gameEntities = gameRepository.findByIdSubjectAcronym(gameSubjectAcronym);
                }
            }
        }
        else {
            if (gameCourse != null) {
                if (gamePeriod != null) {
                    gameEntities = gameRepository.findByIdCourseAndIdPeriod(gameCourse, PeriodType.fromString(gamePeriod));
                }
                else {
                    gameEntities = gameRepository.findByIdCourse(gameCourse);
                }
            }
            else {
                if (gamePeriod != null) {
                    gameEntities = gameRepository.findByIdPeriod(PeriodType.fromString(gamePeriod));
                }
                else {
                    gameEntities = gameRepository.findAll();
                }
            }
        }

        return gameEntities.stream().map((GameMapper::mapToGameDto))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void evaluateGame(String gameSubjectAcronym, Integer gameCourse, String gamePeriod, LocalDate evaluationDate) {
        PeriodType gamePeriodType = PeriodType.fromString(gamePeriod);
        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setCourse(gameCourse);
        gameKey.setPeriod(gamePeriodType);
        GameEntity gameEntity = getGameEntityByKey(gameKey);

        if (gameEntity.getState().equals(StateType.Preparation) || gameEntity.getState().equals(StateType.Finished)) {
            throw new ConstraintViolationException("Game cannot be evaluated because its state is '" + gameEntity.getState().toString() + "' and not 'Playing', please try again only inside date range [" + gameEntity.getStartDate() + "," + gameEntity.getEndDate() + "].");
        }

        Timestamp timestamp = Timestamp.valueOf(evaluationDate.atStartOfDay());

        List<PlayerEntity> teamPlayerEntities = new ArrayList<>();
        List<PlayerEntity> individualPlayerEntities = new ArrayList<>();
        for (GameGroupEntity gameGroupEntity : gameEntity.getGameGroupEntities()) {
            for (ProjectEntity projectEntity : gameGroupEntity.getProjectEntities()) {
                TeamPlayerEntity teamPlayerEntity = projectEntity.getTeamPlayerEntity();
                teamPlayerEntities.add(teamPlayerEntity);
                individualPlayerEntities.addAll(teamPlayerEntity.getIndividualPlayerEntities());
            }
        }

        for (RuleEntity ruleEntity : gameEntity.getRuleEntities()) {
            try {
                if (ruleEntity.checkRuleValidity(Date.valueOf(timestamp.toLocalDateTime().toLocalDate()))) {
                    PlayerType level = ruleEntity.getEvaluableActionEntity().getAssessmentLevel();
                    logger.info("Evaluating rule: {} for level: {}", ruleEntity.getEvaluableActionEntity().getId(), level);
                    
                    if (ruleEntity.getEvaluableActionEntity().getAssessmentLevel().equals(PlayerType.Team)) {
                        for (PlayerEntity playerEntity : teamPlayerEntities) {
                            logger.info("Evaluating team player: {}", playerEntity.getPlayername());
                            evaluateRule(ruleEntity, playerEntity, timestamp);
                        }
                    } else { //PlayerType.Individual
                        for (PlayerEntity playerEntity : individualPlayerEntities) {
                            logger.info("Evaluating individual player: {}", playerEntity.getPlayername());
                            evaluateRule(ruleEntity, playerEntity, timestamp);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error evaluating rule with evaluable action: " + ruleEntity.getEvaluableActionEntity().getId()
                        + ". Make sure when using rules with strategic indicators or quality factors that these are defined for all teams on the LD.");
            }
        }
    }

    @Override
    public GameDTO createGame(String subjectAcronym, Integer course, String period, Date startDate, Date endDate, Float firstLevelPolicyParameter, Float secondLevelPolicyParameter, Float thirdLevelPolicyParameter){
        GameEntity gameEntity = new GameEntity();
        if (subjectAcronym.isBlank() || course == null || period.isBlank() || startDate == null || endDate == null)
            throw new ConstraintViolationException("Game attributes cannot be blank");
        if (startDate.after(endDate))
            throw new ConstraintViolationException("Start date cannot be posterior to the end date, please introduce different dates.");

        SubjectEntity subjectEntity = subjectRepository.findById(subjectAcronym).
                orElseThrow(() -> new ResourceNotFoundException("Subject " + subjectAcronym + " not found."));

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(subjectAcronym);
        gameKey.setCourse(course);
        gameKey.setPeriod(PeriodType.fromString(period));

        if (gameRepository.existsById(gameKey)) {
            throw new ConstraintViolationException("This game already exists.");
        }

        gameEntity.setId(gameKey);
        gameEntity.setStartDate(startDate);
        gameEntity.setEndDate(endDate);
        gameEntity.setSubjectEntity(subjectEntity);
        gameEntity.setLevelPolicyFunctionParameters(new ArrayList<>());
        gameEntity.getLevelPolicyFunctionParameters().add(firstLevelPolicyParameter);
        gameEntity.getLevelPolicyFunctionParameters().add(secondLevelPolicyParameter);
        gameEntity.getLevelPolicyFunctionParameters().add(thirdLevelPolicyParameter);

        GameEntity savedGameEntity;
        try{
            savedGameEntity = gameRepository.save(gameEntity);
        }
        catch(Exception exception){
            if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException)
                throw new ConstraintViolationException("Game with given parameters already exists.");
            else throw exception;
        }
        return GameMapper.mapToGameDto(savedGameEntity);
    }

    @Override
    public GameDTO updateGame(String subjectAcronym, Integer course, String period, Date startDate, Date endDate){
        if (startDate == null || endDate == null)
            throw new ConstraintViolationException("Game attributes cannot be blank");

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(subjectAcronym);
        gameKey.setCourse(course);
        gameKey.setPeriod(PeriodType.fromString(period));
        GameEntity gameEntity = getGameEntityByKey(gameKey);

        gameEntity.setStartDate(startDate);
        gameEntity.setEndDate(endDate);

        GameEntity updatedGameEntity = gameRepository.save(gameEntity);
        return GameMapper.mapToGameDto(updatedGameEntity);
    }
}
