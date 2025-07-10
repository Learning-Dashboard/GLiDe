package edu.upc.gessi.glidegamificationengine.controller;

import edu.upc.gessi.glidegamificationengine.dto.DateRuleDTO;
import edu.upc.gessi.glidegamificationengine.dto.SimpleRuleDTO;
import edu.upc.gessi.glidegamificationengine.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
@CrossOrigin(origins = "http://localhost:4202")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @Operation(summary = "Create simple rule", description = "Create a new simple rule. The simple rule name cannot be blank. The simple rule repetitions must be greater than or equal to 1. The achievement assignment condition name must be a valid condition type (ValueGreaterThan, ValueLessThan, ValueEqualTo, ValueGreaterThanOrEqualTo, ValueLessThanOrEqualTo, ValueOutsideOfRange or ValueInsideOfRange). The number of achievement assignment condition parameters must be the expected by the condition type. The achievement assignment assessment level name must be a valid player type (Team or Individual). The game is identified by the subject acronym, course and period, being the period name a valid period type (Quadrimester1 or Quadrimester2). The evaluable action is identified by its id. The achievement is identified by its id. The simple rule is returned as a SimpleRuleDTO object.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED: SimpleRuleDTO object.", content = @Content(schema = @Schema(implementation = SimpleRuleDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: (1) The given game period name not a valid period type (Only available: Quadrimester1, Quadrimester2); (2) The given achievement assignment condition name not a valid condition type (Only available: ValueGreaterThan, ValueLessThan, ValueEqualTo, ValueGreaterThanOrEqualTo, ValueLessThanOrEqualTo, ValueOutsideOfRange, ValueInsideOfRange); (3) The given number of achievement assignment condition parameters not the expected by the condition type; (4) The given achievement assignment assessment level name not a valid player type (Only available: Team, Individual).", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: (1) Game with the given subject acronym, course and period not found; (2) Evaluable action with the given id not found; (3) Achievement with the given id not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "CONFLICT: (1) Simple rule name cannot be blank; (2) Simple rule repetitions cannot be less than 1.", content = @Content)
    })
    @PostMapping(value="/simples", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRuleDTO> createSimpleRule(@RequestPart(value = "name") String simpleRuleName,
                                                          @RequestPart(value = "repetitions") Integer simpleRuleRepetitions,
                                                          @RequestPart(value = "gameSubjectAcronym") String gameSubjectAcronym,
                                                          @RequestPart(value = "gameCourse") Integer gameCourse,
                                                          @RequestPart(value = "gamePeriod") String gamePeriod,
                                                          @RequestPart(value = "evaluableActionId") String evaluableActionId,
                                                          @RequestPart(value = "achievementId") Long achievementId,
                                                          @RequestPart(value = "achievementAssignmentMessage") String achievementAssignmentMessage,
                                                          @RequestPart(value = "achievementAssignmentOnlyFirstTime") Boolean achievementAssignmentOnlyFirstTime,
                                                          @RequestPart(value = "achievementAssignmentCondition") String achievementAssignmentCondition,
                                                          @RequestPart(value = "achievementAssignmentConditionParameters", required = false) List<Float> achievementAssignmentConditionParameters,
                                                          @RequestPart(value = "achievementAssignmentUnits") Integer achievementAssignmentUnits,
                                                          @RequestPart(value = "achievementAssignmentAssessmentLevel") String achievementAssignmentAssessmentLevel) {
        SimpleRuleDTO savedSimpleRuleDto = ruleService.createSimpleRule(simpleRuleName, simpleRuleRepetitions, gameSubjectAcronym, gameCourse, gamePeriod, evaluableActionId, achievementId, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentCondition, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevel);
        return new ResponseEntity<>(savedSimpleRuleDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get simple rules", description = "Get all the simple rules optionally filtered by a specific game. The game is identified by the subject acronym, course and period when given, being the period name a valid period type (Quadrimester1 or Quadrimester2) (if not given, simple rules from all games are going to be retrieved). The simple rules are returned as a list of SimpleRuleDTO objects.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: List of SimpleRuleDTO objects.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SimpleRuleDTO.class)))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: (1) Game not fully identified (Missing: subject acronym, course or period); (2) The given period name not a valid period type (Only available: Quadrimester1, Quadrimester2).", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Game with the given subject acronym, course and period not found.", content = @Content)
    })
    @GetMapping(value="/simples", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SimpleRuleDTO>> getSimpleRules(@RequestParam(value = "gameSubjectAcronym", required = false) String gameSubjectAcronym,
                                                              @RequestParam(value = "gameCourse", required = false) Integer gameCourse,
                                                              @RequestParam(value = "gamePeriod", required = false) String gamePeriod) {
        List<SimpleRuleDTO> simpleRuleDtos = ruleService.getSimpleRules(gameSubjectAcronym, gameCourse, gamePeriod);
        return ResponseEntity.ok(simpleRuleDtos);
    }

    @Operation(summary = "Get simple rule", description = "Get a simple rule. The simple rule is identified by its id. The simple rule is returned as a SimpleRuleDTO object.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: SimpleRuleDTO object.", content = @Content(schema = @Schema(implementation = SimpleRuleDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Simple rule with the given id not found.", content = @Content)
    })
    @GetMapping(value="/simples/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRuleDTO> getSimpleRule(@PathVariable("id") Long simpleRuleId) {
        SimpleRuleDTO simpleRuleDto = ruleService.getSimpleRule(simpleRuleId);
        return ResponseEntity.ok(simpleRuleDto);
    }

    @Operation(summary = "Delete simple rule", description = "Delete a simple rule and all the associated achievement assignments and logged achievements. The simple rule is identified by its id.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Success message.", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Simple rule with the given id not found.", content = @Content)
    })
    @DeleteMapping(value="/simples/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteSimpleRule(@PathVariable("id") Long simpleRuleId) {
        ruleService.deleteSimpleRule(simpleRuleId);
        return ResponseEntity.ok("Simple rule with id '" + simpleRuleId + "' successfully deleted.");
    }

    @Operation(summary = "Update simple rule", description = "Update a simple rule. The simple rule name cannot be blank. The simple rule repetitions must be greater than or equal to 1. The achievement assignment condition name must be a valid condition type (ValueGreaterThan, ValueLessThan, ValueEqualTo, ValueGreaterThanOrEqualTo, ValueLessThanOrEqualTo, ValueOutsideOfRange or ValueInsideOfRange). The number of achievement assignment condition parameters must be the expected by the condition type. The achievement assignment assessment level name must be a valid player type (Team or Individual). The game is identified by the subject acronym, course and period, being the period name a valid period type (Quadrimester1 or Quadrimester2). The evaluable action is identified by its id. The achievement is identified by its id. The simple rule is returned as a SimpleRuleDTO object.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UPDATED: SimpleRuleDTO object.", content = @Content(schema = @Schema(implementation = SimpleRuleDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: (1) The given game period name not a valid period type (Only available: Quadrimester1, Quadrimester2); (2) The given achievement assignment condition name not a valid condition type (Only available: ValueGreaterThan, ValueLessThan, ValueEqualTo, ValueGreaterThanOrEqualTo, ValueLessThanOrEqualTo, ValueOutsideOfRange, ValueInsideOfRange); (3) The given number of achievement assignment condition parameters not the expected by the condition type; (4) The given achievement assignment assessment level name not a valid player type (Only available: Team, Individual).", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: (1) Game with the given subject acronym, course and period not found; (2) Evaluable action with the given id not found; (3) Achievement with the given id not found; (4) No simple rule exists with the given Id.", content = @Content),
            @ApiResponse(responseCode = "409", description = "CONFLICT: (1) Simple rule name cannot be blank; (2) Simple rule repetitions cannot be less than 1.", content = @Content)
    })
    @PutMapping(value="/simples/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRuleDTO> updateSimpleRule(@PathVariable("id") Long simpleRuleId,
                                                          @RequestPart(value = "name") String simpleRuleName,
                                                          @RequestPart(value = "repetitions") Integer simpleRuleRepetitions,
                                                          @RequestPart(value = "gameSubjectAcronym") String gameSubjectAcronym,
                                                          @RequestPart(value = "gameCourse") Integer gameCourse,
                                                          @RequestPart(value = "gamePeriod") String gamePeriod,
                                                          @RequestPart(value = "evaluableActionId") String evaluableActionId,
                                                          @RequestPart(value = "achievementAssignmentMessage") String achievementAssignmentMessage,
                                                          @RequestPart(value = "achievementAssignmentOnlyFirstTime") Boolean achievementAssignmentOnlyFirstTime,
                                                          @RequestPart(value = "achievementAssignmentCondition") String achievementAssignmentCondition,
                                                          @RequestPart(value = "achievementAssignmentConditionParameters", required = false) List<Float> achievementAssignmentConditionParameters,
                                                          @RequestPart(value = "achievementAssignmentUnits") Integer achievementAssignmentUnits,
                                                          @RequestPart(value = "achievementAssignmentAssessmentLevel") String achievementAssignmentAssessmentLevel) {
        SimpleRuleDTO updatedSimpleRuleDto = ruleService.updateSimpleRule(simpleRuleId, simpleRuleName, simpleRuleRepetitions, gameSubjectAcronym, gameCourse, gamePeriod, evaluableActionId, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentCondition, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevel);
        return ResponseEntity.ok(updatedSimpleRuleDto);
    }

    @Operation(summary = "Create date rule", description = "Create a new date rule. The date rule name cannot be blank. The date rule repetitions must be greater than or equal to 1. The date rule start date must be equal to or previous to the date rule end date. The achievement assignment condition name must be a valid condition type (ValueGreaterThan, ValueLessThan, ValueEqualTo, ValueGreaterThanOrEqualTo, ValueLessThanOrEqualTo, ValueOutsideOfRange or ValueInsideOfRange). The number of achievement assignment condition parameters must be the expected by the condition type. The achievement assignment assessment level name must be a valid player type (Team or Individual). The game is identified by the subject acronym, course and period, being the period name a valid period type (Quadrimester1 or Quadrimester2). The evaluable action is identified by its id. The achievement is identified by its id. The date rule is returned as a DateRuleDTO object.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED: DateRuleDTO object.", content = @Content(schema = @Schema(implementation = DateRuleDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: (1) The given game period name not a valid period type (Only available: Quadrimester1, Quadrimester2); (2) The given achievement assignment condition name not a valid condition type (Only available: ValueGreaterThan, ValueLessThan, ValueEqualTo, ValueGreaterThanOrEqualTo, ValueLessThanOrEqualTo, ValueOutsideOfRange, ValueInsideOfRange); (3) The given number of achievement assignment condition parameters not the expected by the condition type; (4) The given achievement assignment assessment level name not a valid player type (Only available: Team, Individual).", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: (1) Game with the given subject acronym, course and period not found; (2) Evaluable action with the given id not found; (3) Achievement with the given id not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "CONFLICT: (1) Date rule name cannot be blank; (2) Date rule repetitions cannot be less than 1; (3) Date rule start date cannot be posterior to date rule end date.", content = @Content)
    })
    @PostMapping(value="/dates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DateRuleDTO> createDateRule(@RequestPart(value = "name") String dateRuleName,
                                                      @RequestPart(value = "repetitions") Integer dateRuleRepetitions,
                                                      @RequestPart(value = "startDate") @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd") String dateRuleStartDate,
                                                      @RequestPart(value = "endDate") @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd") String dateRuleEndDate,
                                                      @RequestPart(value = "gameSubjectAcronym") String gameSubjectAcronym,
                                                      @RequestPart(value = "gameCourse") Integer gameCourse,
                                                      @RequestPart(value = "gamePeriod") String gamePeriod,
                                                      @RequestPart(value = "evaluableActionId") String evaluableActionId,
                                                      @RequestPart(value = "achievementId") Long achievementId,
                                                      @RequestPart(value = "achievementAssignmentMessage") String achievementAssignmentMessage,
                                                      @RequestPart(value = "achievementAssignmentOnlyFirstTime") Boolean achievementAssignmentOnlyFirstTime,
                                                      @RequestPart(value = "achievementAssignmentCondition") String achievementAssignmentCondition,
                                                      @RequestPart(value = "achievementAssignmentConditionParameters", required = false) List<Float> achievementAssignmentConditionParameters,
                                                      @RequestPart(value = "achievementAssignmentUnits") Integer achievementAssignmentUnits,
                                                      @RequestPart(value = "achievementAssignmentAssessmentLevel") String achievementAssignmentAssessmentLevel) {
        DateRuleDTO savedDateRuleDto = ruleService.createDateRule(dateRuleName, dateRuleRepetitions, Date.valueOf(dateRuleStartDate), Date.valueOf(dateRuleEndDate), gameSubjectAcronym, gameCourse, gamePeriod, evaluableActionId, achievementId, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentCondition, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevel);
        return new ResponseEntity<>(savedDateRuleDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get date rules", description = "Get all the date rules optionally filtered by a specific game. The game is identified by the subject acronym, course and period when given, being the period name a valid period type (Quadrimester1 or Quadrimester2) (if not given, date rules from all games are going to be retrieved). The date rules are returned as a list of DateRuleDTO objects.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: List of DateRuleDTO objects.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DateRuleDTO.class)))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: (1) Game not fully identified (Missing: subject acronym, course or period); (2) The given period name not a valid period type (Only available: Quadrimester1, Quadrimester2).", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Game with the given subject acronym, course and period not found.", content = @Content)
    })
    @GetMapping(value="/dates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DateRuleDTO>> getDateRules(@RequestParam(value = "gameSubjectAcronym", required = false) String gameSubjectAcronym,
                                                          @RequestParam(value = "gameCourse", required = false) Integer gameCourse,
                                                          @RequestParam(value = "gamePeriod", required = false) String gamePeriod) {
        List<DateRuleDTO> dateRuleDtos = ruleService.getDateRules(gameSubjectAcronym, gameCourse, gamePeriod);
        return ResponseEntity.ok(dateRuleDtos);
    }

    @Operation(summary = "Get date rule", description = "Get a date rule. The date rule is identified by its id. The date rule is returned as a DateRuleDTO object.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: DateRuleDTO object.", content = @Content(schema = @Schema(implementation = DateRuleDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Date rule with the given id not found.", content = @Content)
    })
    @GetMapping(value="/dates/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DateRuleDTO> getDateRule(@PathVariable("id") Long dateRuleId) {
        DateRuleDTO dateRuleDto = ruleService.getDateRule(dateRuleId);
        return ResponseEntity.ok(dateRuleDto);
    }

    @Operation(summary = "Delete date rule", description = "Delete a date rule and all the associated achievement assignments and logged achievements. The date rule is identified by its id.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Success message.", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Date rule with the given id not found.", content = @Content)
    })
    @DeleteMapping(value="/dates/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteDateRule(@PathVariable("id") Long dateRuleId) {
        ruleService.deleteDateRule(dateRuleId);
        return ResponseEntity.ok("Simple rule with id '" + dateRuleId + "' successfully deleted.");
    }

    @Operation(summary = "Update date rule", description = "Update a date rule. The date rule name cannot be blank. The date rule repetitions must be greater than or equal to 1. The date rule start date must be equal to or previous to the date rule end date. The achievement assignment condition name must be a valid condition type (ValueGreaterThan, ValueLessThan, ValueEqualTo, ValueGreaterThanOrEqualTo, ValueLessThanOrEqualTo, ValueOutsideOfRange or ValueInsideOfRange). The number of achievement assignment condition parameters must be the expected by the condition type. The achievement assignment assessment level name must be a valid player type (Team or Individual). The game is identified by the subject acronym, course and period, being the period name a valid period type (Quadrimester1 or Quadrimester2). The evaluable action is identified by its id. The achievement is identified by its id. The date rule is returned as a DateRuleDTO object.", tags = { "rules" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UPDATED: DateRuleDTO object.", content = @Content(schema = @Schema(implementation = DateRuleDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: (1) The given game period name not a valid period type (Only available: Quadrimester1, Quadrimester2); (2) The given achievement assignment condition name not a valid condition type (Only available: ValueGreaterThan, ValueLessThan, ValueEqualTo, ValueGreaterThanOrEqualTo, ValueLessThanOrEqualTo, ValueOutsideOfRange, ValueInsideOfRange); (3) The given number of achievement assignment condition parameters not the expected by the condition type; (4) The given achievement assignment assessment level name not a valid player type (Only available: Team, Individual).", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: (1) Game with the given subject acronym, course and period not found; (2) Evaluable action with the given id not found; (3) Achievement with the given id not found; (4) No date rule exists with the given Id.", content = @Content),
            @ApiResponse(responseCode = "409", description = "CONFLICT: (1) Date rule name cannot be blank; (2) Date rule repetitions cannot be less than 1; (3) Date rule start date cannot be posterior to date rule end date.", content = @Content)
    })
    @PutMapping(value="/dates/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DateRuleDTO> updateDateRule(@PathVariable("id") Long dateRuleId,
                                                      @RequestPart(value = "name") String dateRuleName,
                                                      @RequestPart(value = "repetitions") Integer dateRuleRepetitions,
                                                      @RequestPart(value = "startDate") @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd") String dateRuleStartDate,
                                                      @RequestPart(value = "endDate") @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd") String dateRuleEndDate,
                                                      @RequestPart(value = "gameSubjectAcronym") String gameSubjectAcronym,
                                                      @RequestPart(value = "gameCourse") Integer gameCourse,
                                                      @RequestPart(value = "gamePeriod") String gamePeriod,
                                                      @RequestPart(value = "evaluableActionId") String evaluableActionId,
                                                      @RequestPart(value = "achievementAssignmentMessage") String achievementAssignmentMessage,
                                                      @RequestPart(value = "achievementAssignmentOnlyFirstTime") Boolean achievementAssignmentOnlyFirstTime,
                                                      @RequestPart(value = "achievementAssignmentCondition") String achievementAssignmentCondition,
                                                      @RequestPart(value = "achievementAssignmentConditionParameters", required = false) List<Float> achievementAssignmentConditionParameters,
                                                      @RequestPart(value = "achievementAssignmentUnits") Integer achievementAssignmentUnits,
                                                      @RequestPart(value = "achievementAssignmentAssessmentLevel") String achievementAssignmentAssessmentLevel) {
        System.out.println(dateRuleStartDate);
        DateRuleDTO updatedDateRuleDto = ruleService.updateDateRule(dateRuleId, dateRuleName, dateRuleRepetitions, Date.valueOf(dateRuleStartDate), Date.valueOf(dateRuleEndDate), gameSubjectAcronym, gameCourse, gamePeriod, evaluableActionId, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentCondition, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevel);
        return ResponseEntity.ok(updatedDateRuleDto);
    }
}
