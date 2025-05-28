package edu.upc.gessi.glidegamificationengine.controller;

import edu.upc.gessi.glidegamificationengine.dto.GameDTO;
import edu.upc.gessi.glidegamificationengine.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "http://localhost:4202")
public class GameController {

    @Autowired
    private GameService gameService;

    @Operation(summary = "Get games", description = "Get all the games optionally filtered by a specific subject acronym, course and/or period, being the period name a valid period type (Quadrimester1 or Quadrimester2). The games are returned as a list of GameDTO objects.", tags = { "games" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: List of GameDTO objects.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GameDTO.class)))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: The given period name not a valid period type (Only available: Quadrimester1, Quadrimester2).", content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDTO>> getGames(@RequestParam(value = "subjectAcronym", required = false) String gameSubjectAcronym,
                                                  @RequestParam(value = "course", required = false) Integer gameCourse,
                                                  @RequestParam(value = "period", required = false) String gamePeriod) {
        List<GameDTO> gameDtos = gameService.getGames(gameSubjectAcronym, gameCourse, gamePeriod);
        return ResponseEntity.ok(gameDtos);
    }

    @Operation(summary = "Evaluate game", description = "Evaluate the rules of a game to assign achievements to players when their actions meet some conditions. The game is identified by the subject acronym, course and period. The period name must be a valid period type (Quadrimester1 or Quadrimester2).", tags = { "games" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Success message.", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: The given period name not a valid period type (Only available: Quadrimester1, Quadrimester2).", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Game with the given subject acronym, course and period not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "CONFLICT: Game cannot be evaluated because its state is either 'Preparation' or 'Finished', and not 'Playing'.", content = @Content)
    })
    @GetMapping(value ="/evaluate", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> evaluateGame(@RequestParam(value = "subjectAcronym") String gameSubjectAcronym,
                                               @RequestParam(value = "course") Integer gameCourse,
                                               @RequestParam(value = "period") String gamePeriod,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate evaluationDate) {
        if (evaluationDate == null) {
            evaluationDate = LocalDate.now();
        }
        gameService.evaluateGame(gameSubjectAcronym, gameCourse, gamePeriod, evaluationDate);
        return ResponseEntity.ok("Game corresponding to subject with acronym '" + gameSubjectAcronym + "', course '" + gameCourse + "' period '" + gamePeriod + "', and date '" + evaluationDate + "' successfully evaluated.");
    }

    @Operation(summary = "Create game", description = "Create new game from given parameters.", tags = { "games" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED: Game.", content = @Content(schema = @Schema(implementation = GameDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Subject with the given subject acronym not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "CONFLICT: (1) Game parameters cannot be blank. (2) The given game already exists.", content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> createGame(@RequestPart(value = "subjectAcronym") String subjectAcronym,
                                              @RequestPart(value = "course") Integer course,
                                              @RequestPart(value = "period") String period,
                                              @RequestPart(value = "startDate") @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd") String startDate,
                                              @RequestPart(value = "endDate") @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd") String endDate,
                                              @RequestPart(value = "firstLevelPolicyParameter") Float firstLevelPolicyParameter,
                                              @RequestPart(value = "secondLevelPolicyParameter") Float secondLevelPolicyParameter,
                                              @RequestPart(value = "thirdLevelPolicyParameter") Float thirdLevelPolicyParameter){
        GameDTO savedGameDto = gameService.createGame(subjectAcronym, course, period, Date.valueOf(startDate), Date.valueOf(endDate), firstLevelPolicyParameter, secondLevelPolicyParameter, thirdLevelPolicyParameter);
        return new ResponseEntity<>(savedGameDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Update game", description = "Update game with given parameters.", tags = { "games" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Game updated.", content = @Content(schema = @Schema(implementation = GameDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Game with the given parameters not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "CONFLICT: (1) Invalid date parameters.", content = @Content)
    })
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> updateGame(@RequestParam(value = "gameSubjectAcronym") String gameSubjectAcronym,
                                              @RequestParam(value = "gameCourse") Integer gameCourse,
                                              @RequestParam(value = "gamePeriod") String gamePeriod,
                                              @RequestPart(value = "startDate") @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd") String startDate,
                                              @RequestPart(value = "endDate") @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd") String endDate){
        GameDTO updatedGame = gameService.updateGame(gameSubjectAcronym, gameCourse, gamePeriod, Date.valueOf(startDate), Date.valueOf(endDate));
        return new ResponseEntity<>(updatedGame, HttpStatus.OK);
    }
}
