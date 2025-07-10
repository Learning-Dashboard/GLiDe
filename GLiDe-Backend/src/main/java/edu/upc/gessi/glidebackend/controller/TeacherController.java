package edu.upc.gessi.glidebackend.controller;

import edu.upc.gessi.glidebackend.dto.TeacherUserDto;
import edu.upc.gessi.glidebackend.dto.TeacherGameDto;
import edu.upc.gessi.glidebackend.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = {"http://localhost:4201", "http://localhost:4202"})
public class TeacherController {

    @Autowired
    private TeacherService teacherService;    // POST /api/teachers/login - Login de profesor
    @PostMapping(value = "/login")
    public ResponseEntity<?> postLogin(@RequestHeader(HttpHeaders.AUTHORIZATION) String idToken) {
        teacherService.getTeacher(idToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // GET /api/teachers/profile - Obtenir perfil del professor autenticat
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeacherUserDto> getTeacherProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String idToken) {
        TeacherUserDto teacher = teacherService.getTeacher(idToken);
        return ResponseEntity.ok(teacher);
    }

    // GET /api/teachers/games - Obtenir games del professor autenticat
    @GetMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TeacherGameDto>> getTeacherGamesAuth(@RequestHeader(HttpHeaders.AUTHORIZATION) String idToken) {
        TeacherUserDto teacher = teacherService.getTeacher(idToken);
        List<TeacherGameDto> games = teacherService.getTeacherGames(teacher.getEmail());
        return ResponseEntity.ok(games);
    }

    // GET /api/teachers - Obtenir tots els professors
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TeacherUserDto>> getAllTeachers() {
        List<TeacherUserDto> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }    // GET /api/teachers/{email} - Obtenir un professor específic
    @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeacherUserDto> getTeacher(@PathVariable String email) {
        TeacherUserDto teacher = teacherService.getTeacherByEmail(email);
        return ResponseEntity.ok(teacher);
    }

    // POST /api/teachers - Crear un nou professor
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeacherUserDto> createTeacher(@RequestBody TeacherUserDto teacherDto) {
        TeacherUserDto created = teacherService.createTeacher(teacherDto);
        return ResponseEntity.ok(created);
    }

    // PUT /api/teachers/{email} - Actualitzar un professor
    @PutMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeacherUserDto> updateTeacher(@PathVariable String email, @RequestBody TeacherUserDto teacherDto) {
        TeacherUserDto updated = teacherService.updateTeacher(email, teacherDto);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/teachers/{email} - Eliminar un professor
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String email) {
        teacherService.deleteTeacher(email);
        return ResponseEntity.ok().build();
    }

    // GET /api/teachers/{teacherEmail}/games - Obtenir games d'un professor
    @GetMapping(value = "/{teacherEmail}/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TeacherGameDto>> getTeacherGames(@PathVariable String teacherEmail) {
        List<TeacherGameDto> games = teacherService.getTeacherGames(teacherEmail);
        return ResponseEntity.ok(games);
    }

    // POST /api/teachers/{teacherEmail}/games - Assignar game a professor
    @PostMapping(value = "/{teacherEmail}/games")
    public ResponseEntity<TeacherGameDto> assignGameToTeacher(
            @PathVariable String teacherEmail,
            @RequestParam String gameSubjectAcronym,
            @RequestParam Integer gameCourse,
            @RequestParam String gamePeriod) {
        TeacherGameDto assigned = teacherService.assignGameToTeacher(teacherEmail, gameSubjectAcronym, gameCourse, gamePeriod);
        return ResponseEntity.ok(assigned);
    }

    // DELETE /api/teachers/{teacherEmail}/games - Desassignar game de professor
    @DeleteMapping("/{teacherEmail}/games")
    public ResponseEntity<Void> removeGameFromTeacher(
            @PathVariable String teacherEmail,
            @RequestParam String gameSubjectAcronym,
            @RequestParam Integer gameCourse,
            @RequestParam String gamePeriod) {
        teacherService.removeGameFromTeacher(teacherEmail, gameSubjectAcronym, gameCourse, gamePeriod);
        return ResponseEntity.ok().build();
    }    // GET /api/teachers/games/search - Obtenir professors d'un game específic
    @GetMapping(value = "/games/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TeacherGameDto>> getTeachersForGame(
            @RequestParam String gameSubjectAcronym,
            @RequestParam Integer gameCourse,
            @RequestParam String gamePeriod) {
        List<TeacherGameDto> teachers = teacherService.getTeachersForGame(gameSubjectAcronym, gameCourse, gamePeriod);
        return ResponseEntity.ok(teachers);
    }
}