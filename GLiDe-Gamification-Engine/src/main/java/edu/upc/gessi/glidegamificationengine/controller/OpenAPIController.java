package edu.upc.gessi.glidegamificationengine.controller;

import edu.upc.gessi.glidegamificationengine.dto.SimpleRuleDTO;
import edu.upc.gessi.glidegamificationengine.service.OpenAPIService;
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

import java.util.List;

@RestController
@RequestMapping("/api/openAiApi")
@CrossOrigin(origins = "http://localhost:4202")
public class OpenAPIController {

    @Autowired
    private OpenAPIService openAPIService;

    @Operation(summary = "Create rule title and description", description = "Creates rule title and description for .", tags = { "openAPI" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: SimpleRuleDTO object.", content = @Content(schema = @Schema(implementation = SimpleRuleDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: None of the parameters can be blank.", content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRuleDTO> createSubject(@RequestPart(value = "evaluableActionDescription") String evaluableActionDescription,
                                                @RequestPart(value = "condition") String condition,
                                                @RequestPart(value = "conditionParameters") List<Float> conditionParameters){
        SimpleRuleDTO simpleRuleDTO = openAPIService.getSuggestedRuleTitleMessage(evaluableActionDescription, condition, conditionParameters);
        return new ResponseEntity<>(simpleRuleDTO, HttpStatus.OK);
    }
}
