package edu.upc.gessi.glidebackend.controller;

import edu.upc.gessi.glidebackend.dto.IndividualPlayerDto;
import edu.upc.gessi.glidebackend.dto.StudentNicknameDto;
import edu.upc.gessi.glidebackend.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @CrossOrigin(origins = "http://localhost:4201")
    @PostMapping(value="/login")
    public ResponseEntity<?> postLogin(@RequestHeader(HttpHeaders.AUTHORIZATION) String idToken) {
        studentService.getStudent(idToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/players")
    public ResponseEntity<?> getStudentPlayers(@RequestHeader(HttpHeaders.AUTHORIZATION) String idToken) {
        List<IndividualPlayerDto> individualPlayerDto = studentService.getStudentPlayers(idToken);
        return ResponseEntity.ok(individualPlayerDto);
    }

    @CrossOrigin(origins = {"http://localhost:4201", "http://localhost:4202"})
    @GetMapping(value="/game/{subjectAcronym}/{course}/{period}")
    public ResponseEntity<List<IndividualPlayerDto>> getStudentsByGame(
            @PathVariable String subjectAcronym,
            @PathVariable Integer course,
            @PathVariable String period) {
        List<IndividualPlayerDto> students = studentService.getStudentsByGame(subjectAcronym, course, period);
        return ResponseEntity.ok(students);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @PostMapping(value="/updateNickname")
    public ResponseEntity<?> updateNickname(@RequestHeader(HttpHeaders.AUTHORIZATION) String idToken,
                                            @RequestParam String nickname) {
        studentService.updateNickname(idToken, nickname);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/nicknames")
    public ResponseEntity<List<StudentNicknameDto>> getAllStudentNicknames() {
        List<StudentNicknameDto> students = studentService.getAllStudentNicknames();
        return ResponseEntity.ok(students);
    }
}
