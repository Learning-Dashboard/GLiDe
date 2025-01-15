package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.DateRuleDTO;
import edu.upc.gessi.glidegamificationengine.dto.SimpleRuleDTO;
import edu.upc.gessi.glidegamificationengine.entity.*;
import edu.upc.gessi.glidegamificationengine.entity.key.GameKey;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.MissingInformationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.mapper.RuleMapper;
import edu.upc.gessi.glidegamificationengine.repository.DateRuleRepository;
import edu.upc.gessi.glidegamificationengine.repository.SimpleRuleRepository;
import edu.upc.gessi.glidegamificationengine.service.RuleService;
import edu.upc.gessi.glidegamificationengine.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    private SimpleRuleRepository simpleRuleRepository;

    @Autowired
    private DateRuleRepository dateRuleRepository;

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private EvaluableActionServiceImpl evaluableActionService;

    @Autowired
    private AchievementServiceImpl achievementService;

    @Autowired
    private AchievementAssignmentServiceImpl achievementAssignmentService;

    @Autowired
    private PlayerServiceImpl playerService;

    /* Methods callable from Service Layer */


    /* Methods callable from Controller Layer */

    @Override
    public SimpleRuleDTO createSimpleRule(String simpleRuleName, Integer simpleRuleRepetitions, String gameSubjectAcronym, Integer gameCourse, String gamePeriod, String evaluableActionId, Long achievementId, String achievementAssignmentMessage, Boolean achievementAssignmentOnlyFirstTime, String achievementAssignmentCondition, List<Float> achievementAssignmentConditionParameters, Integer achievementAssignmentUnits, String achievementAssignmentAssessmentLevel) {
        PeriodType gamePeriodType = PeriodType.fromString(gamePeriod);
        ConditionType achievementAssignmentConditionType = ConditionType.fromString(achievementAssignmentCondition);
        if ((achievementAssignmentConditionParameters == null && achievementAssignmentConditionType.getNumberOfRequiredParameters() != 0) || (achievementAssignmentConditionParameters != null && achievementAssignmentConditionParameters.size() != achievementAssignmentConditionType.getNumberOfRequiredParameters())) {
            throw new MissingInformationException("The given " + (achievementAssignmentConditionParameters == null ? "0" : achievementAssignmentConditionParameters.size()) + " achievement assignment condition parameters not the expected number by the condition type " + achievementAssignmentConditionType + " (Expected number of parameters: " + achievementAssignmentConditionType.getNumberOfRequiredParameters() + ").");
        }
        PlayerType achievementAssignmentAssessmentLevelType = PlayerType.fromString(achievementAssignmentAssessmentLevel);

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setCourse(gameCourse);
        gameKey.setPeriod(gamePeriodType);
        GameEntity gameEntity = gameService.getGameEntityByKey(gameKey);
        EvaluableActionEntity evaluableActionEntity = evaluableActionService.getEvaluableActionEntityById(evaluableActionId);
        AchievementEntity achievementEntity = achievementService.getAchievementEntityById(achievementId);

        if (simpleRuleName.isBlank())
            throw new ConstraintViolationException("Simple rule name cannot be blank, please introduce a name.");
        if (simpleRuleRepetitions < 1)
            throw new ConstraintViolationException("Simple rule repetitions cannot be less than 1, please introduce a valid number.");

        SimpleRuleEntity simpleRuleEntity = new SimpleRuleEntity();
        simpleRuleEntity.setName(simpleRuleName);
        simpleRuleEntity.setType(RuleType.Simple);
        simpleRuleEntity.setRepetitions(simpleRuleRepetitions);
        simpleRuleEntity.setGameEntity(gameEntity);
        simpleRuleEntity.setEvaluableActionEntity(evaluableActionEntity);
        SimpleRuleEntity savedSimpleRuleEntity = simpleRuleRepository.save(simpleRuleEntity);
        gameEntity.addRuleEntity(savedSimpleRuleEntity);

        achievementAssignmentService.createAchievementAssignmentEntity(savedSimpleRuleEntity, achievementEntity, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentConditionType, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevelType);

        return RuleMapper.mapToSimpleRuleDto(savedSimpleRuleEntity);
    }

    @Override
    public List<SimpleRuleDTO> getSimpleRules(String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        List<SimpleRuleEntity> simpleRuleEntities;

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
            simpleRuleEntities = simpleRuleRepository.findByGameEntity(gameService.getGameEntityByKey(gameKey));
        }
        else {
            simpleRuleEntities = simpleRuleRepository.findAll();
        }

        return simpleRuleEntities.stream().map((simpleRuleEntity -> RuleMapper.mapToSimpleRuleDto(simpleRuleEntity)))
                .collect(Collectors.toList());
    }

    @Override
    public SimpleRuleDTO getSimpleRule(Long simpleRuleId) {
        SimpleRuleEntity simpleRuleEntity = simpleRuleRepository.findById(simpleRuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Simple rule with id '" + simpleRuleId + "' not found."));
        return RuleMapper.mapToSimpleRuleDto(simpleRuleEntity);
    }

    @Override
    public void deleteSimpleRule(Long simpleRuleId){
        SimpleRuleEntity simpleRuleEntity = simpleRuleRepository.findById(simpleRuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Simple rule with id '" + simpleRuleId + "' not found."));

        boolean achievementCategoryIsPoints = simpleRuleEntity.getAchievementAssignmentEntity().getAchievementEntity().getCategory().equals(AchievementCategoryType.Points);

        simpleRuleRepository.delete(simpleRuleEntity);

        if (achievementCategoryIsPoints)
            playerService.updatePlayersPointsAndLevels();
    }

    @Override
    public SimpleRuleDTO updateSimpleRule(Long simpleRuleId, String simpleRuleName, Integer simpleRuleRepetitions, String gameSubjectAcronym, Integer gameCourse, String gamePeriod, String evaluableActionId, String achievementAssignmentMessage, Boolean achievementAssignmentOnlyFirstTime, String achievementAssignmentCondition, List<Float> achievementAssignmentConditionParameters, Integer achievementAssignmentUnits, String achievementAssignmentAssessmentLevel){
        PeriodType gamePeriodType = PeriodType.fromString(gamePeriod);
        ConditionType achievementAssignmentConditionType = ConditionType.fromString(achievementAssignmentCondition);
        if ((achievementAssignmentConditionParameters == null && achievementAssignmentConditionType.getNumberOfRequiredParameters() != 0) || (achievementAssignmentConditionParameters != null && achievementAssignmentConditionParameters.size() != achievementAssignmentConditionType.getNumberOfRequiredParameters())) {
            throw new MissingInformationException("The given " + (achievementAssignmentConditionParameters == null ? "0" : achievementAssignmentConditionParameters.size()) + " achievement assignment condition parameters not the expected number by the condition type " + achievementAssignmentConditionType + " (Expected number of parameters: " + achievementAssignmentConditionType.getNumberOfRequiredParameters() + ").");
        }
        PlayerType achievementAssignmentAssessmentLevelType = PlayerType.fromString(achievementAssignmentAssessmentLevel);

        SimpleRuleEntity simpleRuleEntity = simpleRuleRepository.findById(simpleRuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Simple rule with id '" + simpleRuleId + "' not found."));

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setCourse(gameCourse);
        gameKey.setPeriod(gamePeriodType);
        GameEntity gameEntity = gameService.getGameEntityByKey(gameKey);
        EvaluableActionEntity evaluableActionEntity = evaluableActionService.getEvaluableActionEntityById(evaluableActionId);

        if (simpleRuleName.isBlank())
            throw new ConstraintViolationException("Simple rule name cannot be blank, please introduce a name.");
        if (simpleRuleRepetitions < 1)
            throw new ConstraintViolationException("Simple rule repetitions cannot be less than 1, please introduce a valid number.");

        simpleRuleEntity.setName(simpleRuleName);
        simpleRuleEntity.setType(RuleType.Simple);
        simpleRuleEntity.setRepetitions(simpleRuleRepetitions);
        simpleRuleEntity.setGameEntity(gameEntity);
        simpleRuleEntity.setEvaluableActionEntity(evaluableActionEntity);
        SimpleRuleEntity updatedSimpleRuleEntity = simpleRuleRepository.save(simpleRuleEntity);

        achievementAssignmentService.updateAchievementAssignmentEntity(updatedSimpleRuleEntity, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentConditionType, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevelType);

        return RuleMapper.mapToSimpleRuleDto(updatedSimpleRuleEntity);
    }

    @Override
    public DateRuleDTO createDateRule(String dateRuleName, Integer dateRuleRepetitions, Date dateRuleStartDate, Date dateRuleEndDate, String gameSubjectAcronym, Integer gameCourse, String gamePeriod, String evaluableActionId, Long achievementId, String achievementAssignmentMessage, Boolean achievementAssignmentOnlyFirstTime, String achievementAssignmentCondition, List<Float> achievementAssignmentConditionParameters, Integer achievementAssignmentUnits, String achievementAssignmentAssessmentLevel) {
        PeriodType gamePeriodType = PeriodType.fromString(gamePeriod);
        ConditionType achievementAssignmentConditionType = ConditionType.fromString(achievementAssignmentCondition);
        if ((achievementAssignmentConditionParameters == null && achievementAssignmentConditionType.getNumberOfRequiredParameters() != 0) || (achievementAssignmentConditionParameters != null && achievementAssignmentConditionParameters.size() != achievementAssignmentConditionType.getNumberOfRequiredParameters())) {
            throw new MissingInformationException("The given " + (achievementAssignmentConditionParameters == null ? "0" : achievementAssignmentConditionParameters.size()) + " achievement assignment condition parameters not the expected number by the condition type " + achievementAssignmentConditionType + " (Expected number of parameters: " + achievementAssignmentConditionType.getNumberOfRequiredParameters() + ").");
        }
        PlayerType achievementAssignmentAssessmentLevelType = PlayerType.fromString(achievementAssignmentAssessmentLevel);

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setCourse(gameCourse);
        gameKey.setPeriod(gamePeriodType);
        GameEntity gameEntity = gameService.getGameEntityByKey(gameKey);
        EvaluableActionEntity evaluableActionEntity = evaluableActionService.getEvaluableActionEntityById(evaluableActionId);
        AchievementEntity achievementEntity = achievementService.getAchievementEntityById(achievementId);

        if (dateRuleName.isBlank())
            throw new ConstraintViolationException("Date rule name cannot be blank, please introduce a name.");
        if (dateRuleRepetitions < 1)
            throw new ConstraintViolationException("Date rule repetitions cannot be less than 1, please introduce a valid number.");
        if (dateRuleStartDate.after(dateRuleEndDate))
            throw new ConstraintViolationException("Date rule start date cannot be posterior to the date rule end date, please introduce different dates.");

        DateRuleEntity dateRuleEntity = new DateRuleEntity();
        dateRuleEntity.setName(dateRuleName);
        dateRuleEntity.setType(RuleType.Date);
        dateRuleEntity.setRepetitions(dateRuleRepetitions);
        dateRuleEntity.setStartDate(dateRuleStartDate);
        dateRuleEntity.setEndDate(dateRuleEndDate);
        dateRuleEntity.setGameEntity(gameEntity);
        dateRuleEntity.setEvaluableActionEntity(evaluableActionEntity);
        DateRuleEntity savedDateRuleEntity = dateRuleRepository.save(dateRuleEntity);
        gameEntity.addRuleEntity(savedDateRuleEntity);

        achievementAssignmentService.createAchievementAssignmentEntity(savedDateRuleEntity, achievementEntity, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentConditionType, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevelType);

        return RuleMapper.mapToDateRuleDto(savedDateRuleEntity);
    }

    @Override
    public List<DateRuleDTO> getDateRules(String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        List<DateRuleEntity> dateRuleEntities;

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
            dateRuleEntities = dateRuleRepository.findByGameEntity(gameService.getGameEntityByKey(gameKey));
        }
        else {
            dateRuleEntities = dateRuleRepository.findAll();
        }

        return dateRuleEntities.stream().map((dateRuleEntity -> RuleMapper.mapToDateRuleDto(dateRuleEntity)))
                .collect(Collectors.toList());
    }

    @Override
    public DateRuleDTO getDateRule(Long dateRuleId) {
        DateRuleEntity dateRuleEntity = dateRuleRepository.findById(dateRuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Date rule with id '" + dateRuleId + "' not found."));
        return RuleMapper.mapToDateRuleDto(dateRuleEntity);
    }

    @Override
    public void deleteDateRule(Long dateRuleId){
        DateRuleEntity dateRuleEntity = dateRuleRepository.findById(dateRuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Simple rule with id '" + dateRuleId + "' not found."));

        boolean achievementCategoryIsPoints = dateRuleEntity.getAchievementAssignmentEntity().getAchievementEntity().getCategory().equals(AchievementCategoryType.Points);

        dateRuleRepository.delete(dateRuleEntity);

        if (achievementCategoryIsPoints)
            playerService.updatePlayersPointsAndLevels();
    }

    @Override
    public DateRuleDTO updateDateRule(Long dateRuleId, String dateRuleName, Integer dateRuleRepetitions, Date dateRuleStartDate, Date dateRuleEndDate, String gameSubjectAcronym, Integer gameCourse, String gamePeriod, String evaluableActionId, String achievementAssignmentMessage, Boolean achievementAssignmentOnlyFirstTime, String achievementAssignmentCondition, List<Float> achievementAssignmentConditionParameters, Integer achievementAssignmentUnits, String achievementAssignmentAssessmentLevel){
        PeriodType gamePeriodType = PeriodType.fromString(gamePeriod);
        ConditionType achievementAssignmentConditionType = ConditionType.fromString(achievementAssignmentCondition);
        if ((achievementAssignmentConditionParameters == null && achievementAssignmentConditionType.getNumberOfRequiredParameters() != 0) || (achievementAssignmentConditionParameters != null && achievementAssignmentConditionParameters.size() != achievementAssignmentConditionType.getNumberOfRequiredParameters())) {
            throw new MissingInformationException("The given " + (achievementAssignmentConditionParameters == null ? "0" : achievementAssignmentConditionParameters.size()) + " achievement assignment condition parameters not the expected number by the condition type " + achievementAssignmentConditionType + " (Expected number of parameters: " + achievementAssignmentConditionType.getNumberOfRequiredParameters() + ").");
        }
        PlayerType achievementAssignmentAssessmentLevelType = PlayerType.fromString(achievementAssignmentAssessmentLevel);

        DateRuleEntity dateRuleEntity = dateRuleRepository.findById(dateRuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Simple rule with id '" + dateRuleId + "' not found."));

        GameKey gameKey = new GameKey();
        gameKey.setSubjectAcronym(gameSubjectAcronym);
        gameKey.setCourse(gameCourse);
        gameKey.setPeriod(gamePeriodType);
        GameEntity gameEntity = gameService.getGameEntityByKey(gameKey);
        EvaluableActionEntity evaluableActionEntity = evaluableActionService.getEvaluableActionEntityById(evaluableActionId);

        if (dateRuleName.isBlank())
            throw new ConstraintViolationException("Date rule name cannot be blank, please introduce a name.");
        if (dateRuleRepetitions < 1)
            throw new ConstraintViolationException("Date rule repetitions cannot be less than 1, please introduce a valid number.");
        if (dateRuleStartDate.after(dateRuleEndDate))
            throw new ConstraintViolationException("Date rule start date cannot be posterior to the date rule end date, please introduce different dates.");

        dateRuleEntity.setName(dateRuleName);
        dateRuleEntity.setType(RuleType.Simple);
        dateRuleEntity.setRepetitions(dateRuleRepetitions);
        dateRuleEntity.setGameEntity(gameEntity);
        dateRuleEntity.setEvaluableActionEntity(evaluableActionEntity);
        dateRuleEntity.setStartDate(dateRuleStartDate);
        dateRuleEntity.setEndDate(dateRuleEndDate);
        DateRuleEntity updatedDateRuleEntity = dateRuleRepository.save(dateRuleEntity);

        achievementAssignmentService.updateAchievementAssignmentEntity(updatedDateRuleEntity, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentConditionType, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevelType);

        return RuleMapper.mapToDateRuleDto(updatedDateRuleEntity);
    }

}
