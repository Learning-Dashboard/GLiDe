package edu.upc.gessi.glidegamificationengine.controller;

import edu.upc.gessi.glidegamificationengine.dto.EvaluableActionDTO;
import edu.upc.gessi.glidegamificationengine.service.EvaluableActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluableActions")
@CrossOrigin(origins = "http://localhost:4202")
public class EvaluableActionController {

    @Autowired
    private EvaluableActionService evaluableActionService;

    @Operation(summary = "Initiate evaluable actions", description = "Initiate the evaluable actions usable from the Learning Dashboard source data tool, which all belong to the Player Performance category.", tags = { "evaluableActions" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Success message.", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping(value="/initiate", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> initiateEvaluableActions() {
        evaluableActionService.initiateEvaluableActions();
        return ResponseEntity.ok("Evaluable actions successfully initiated.");
    }

    @Operation(summary = "Get evaluable actions", description = "Get all the evaluable actions. The evaluable actions are returned as a list of EvaluableActionDTO objects.", tags = { "evaluableActions" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: List of EvaluableActionDTO objects.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EvaluableActionDTO.class))))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EvaluableActionDTO>> getEvaluableActions(){
        List<EvaluableActionDTO> evaluableActionDtos = evaluableActionService.getEvaluableActions();
        return ResponseEntity.ok(evaluableActionDtos);
    }

    @Operation(summary = "Get evaluable action", description = "Get an evaluable action. The evaluable action is identified by its id. The evaluable action is returned as an EvaluableActionDTO object.", tags = { "evaluableActions" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: EvaluableActionDTO object.", content = @Content(schema = @Schema(implementation = EvaluableActionDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Evaluable action with the given id not found.", content = @Content)
    })
    @GetMapping(value="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EvaluableActionDTO> getEvaluableAction(@PathVariable("id") String evaluableActionId) {
        EvaluableActionDTO evaluableActionDto = evaluableActionService.getEvaluableAction(evaluableActionId);
        return ResponseEntity.ok(evaluableActionDto);
    }

}
