package edu.upc.gessi.glidegamificationengine.controller;

import edu.upc.gessi.glidegamificationengine.dto.GameGroupDTO;
import edu.upc.gessi.glidegamificationengine.service.GameGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gameGroups")
@CrossOrigin(origins = "http://localhost:4202")
public class GameGroupController {

    @Autowired
    private GameGroupService gameGroupService;

    @Operation(summary = "Create game group", description = "Creates a group for the indicated game", tags = { "gameGroups" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED: Group for game.", content = @Content(schema = @Schema(implementation = GameGroupDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Game with the given subject acronym, course and period not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "CONFLICT: (1) Game parameters cannot be blank", content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameGroupDTO> createGameGroup(@RequestPart(value = "gameSubjectAcronym") String gameSubjectAcronym,
                                                        @RequestPart(value = "gameCourse") Integer gameCourse,
                                                        @RequestPart(value = "gamePeriod") String gamePeriod,
                                                        @RequestPart(value = "group") Integer group){
        GameGroupDTO savedGameGroupDto = gameGroupService.createGroup(gameCourse,gamePeriod,gameSubjectAcronym,group);
        return new ResponseEntity<>(savedGameGroupDto, HttpStatus.CREATED);
    }
}
