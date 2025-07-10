package edu.upc.gessi.glidegamificationengine.controller;

import edu.upc.gessi.glidegamificationengine.service.ImportDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/importData")
@CrossOrigin(origins = "http://localhost:4202")
public class ImportDataController {

    @Autowired
    private ImportDataService importDataService;

    @Operation(summary = "Import data", description = "Import data", tags = { "importData" })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> importData(@RequestParam(value = "subjectAcronym") String gameSubjectAcronym,
                                 @RequestParam(value = "course") Integer gameCourse,
                                 @RequestParam(value = "period") String gamePeriod,
                                 @RequestParam(value = "groupNumber") Integer groupNumber,
                                 @RequestPart(value = "importedData") MultipartFile importedData) throws IOException {
        importDataService.importData(gameSubjectAcronym, gameCourse, gamePeriod, groupNumber, importedData);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
