package edu.upc.gessi.glidebackend.service;

import edu.upc.gessi.glidebackend.dto.TeacherUserDto;
import edu.upc.gessi.glidebackend.dto.TeacherGameDto;

import java.util.List;

public interface TeacherService {
    
    // Gestió de professors
    List<TeacherUserDto> getAllTeachers();
    TeacherUserDto getTeacher(String email);
    TeacherUserDto createTeacher(TeacherUserDto teacherDto);
    TeacherUserDto updateTeacher(String email, TeacherUserDto teacherDto);
    void deleteTeacher(String email);
    
    // Gestió d'assignacions professor-game
    List<TeacherGameDto> getTeacherGames(String teacherEmail);
    TeacherGameDto assignGameToTeacher(String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
    void removeGameFromTeacher(String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
    List<TeacherGameDto> getTeachersForGame(String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
}