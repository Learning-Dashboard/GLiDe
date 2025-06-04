package edu.upc.gessi.glidebackend.controller;

import edu.upc.gessi.glidebackend.dto.TeacherGameDto;
import edu.upc.gessi.glidebackend.dto.TeacherUserDto;
import edu.upc.gessi.glidebackend.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "http://localhost:4201")
public class TeacherController {

    private TeacherService teacherService;

    @GetMapping(value = "/{teacherEmail}/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TeacherGameDto>> getTeacherGames(@PathVariable("teacherEmail") String teacherEmail) {
        List<TeacherGameDto> games = teacherService.getTeacherGames(teacherEmail);
        return ResponseEntity.ok(games);
    }

    @PostMapping(value = "/{teacherEmail}/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeacherGameDto> assignGameToTeacher(
            @PathVariable("teacherEmail") String teacherEmail,
            @RequestParam("gameSubjectAcronym") String gameSubjectAcronym,
            @RequestParam("gameCourse") Integer gameCourse,
            @RequestParam("gamePeriod") String gamePeriod) {
        
        TeacherGameDto assigned = teacherService.assignGameToTeacher(teacherEmail, gameSubjectAcronym, gameCourse, gamePeriod);
        return ResponseEntity.ok(assigned);
    }

    @DeleteMapping(value = "/{teacherEmail}/games")
    public ResponseEntity<Void> removeGameFromTeacher(
            @PathVariable("teacherEmail") String teacherEmail,
            @RequestParam("gameSubjectAcronym") String gameSubjectAcronym,
            @RequestParam("gameCourse") Integer gameCourse,
            @RequestParam("gamePeriod") String gamePeriod) {
        
        teacherService.removeGameFromTeacher(teacherEmail, gameSubjectAcronym, gameCourse, gamePeriod);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/games/{gameSubjectAcronym}/{gameCourse}/{gamePeriod}/teachers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TeacherUserDto>> getTeachersForGame(
            @PathVariable("gameSubjectAcronym") String gameSubjectAcronym,
            @PathVariable("gameCourse") Integer gameCourse,
            @PathVariable("gamePeriod") String gamePeriod) {
        
        List<TeacherUserDto> teachers = teacherService.getTeachersForGame(gameSubjectAcronym, gameCourse, gamePeriod);
        return ResponseEntity.ok(teachers);
    }
}