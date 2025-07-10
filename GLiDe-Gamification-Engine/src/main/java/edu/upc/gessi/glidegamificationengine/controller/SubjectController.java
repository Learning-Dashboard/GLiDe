package edu.upc.gessi.glidegamificationengine.controller;

import edu.upc.gessi.glidegamificationengine.dto.SubjectDTO;
import edu.upc.gessi.glidegamificationengine.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "http://localhost:4202")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Operation(summary = "Get subjects", description = "Get all the subjects", tags = { "subjects" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: List of SubjectDTO objects", content= @Content(array = @ArraySchema(schema = @Schema(implementation = SubjectDTO.class))))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SubjectDTO>> getSubjects() {
        List<SubjectDTO> subjectDtos = subjectService.getSubjects();
        return ResponseEntity.ok(subjectDtos);
    }

    @Operation(summary = "Create subject", description = "Create a new subject.", tags = { "subjects" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED: AchievementDTO object.", content = @Content(schema = @Schema(implementation = SubjectDTO.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT: (1) Subject acronym cannot be blank. (2) The given subject acronym is already used.", content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectDTO> createSubject(@RequestPart(value = "acronym") String acronym,
                                                    @RequestPart(value = "code") Integer code,
                                                    @RequestPart(value = "name") String name,
                                                    @RequestPart(value = "school") String school,
                                                    @RequestPart(value = "studies") String studies){
        SubjectDTO savedSubjectDto = subjectService.createSubject(acronym, code, name, school, studies);
        return new ResponseEntity<>(savedSubjectDto, HttpStatus.CREATED);
    }
}
