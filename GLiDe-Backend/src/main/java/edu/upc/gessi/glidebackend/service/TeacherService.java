package edu.upc.gessi.glidebackend.service;

import edu.upc.gessi.glidebackend.dto.TeacherGameDto;
import edu.upc.gessi.glidebackend.dto.TeacherUserDto;

import java.util.List;

public interface TeacherService {
    List<TeacherGameDto> getTeacherGames(String teacherEmail);
    TeacherGameDto assignGameToTeacher(String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
    void removeGameFromTeacher(String teacherEmail, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
    List<TeacherUserDto> getTeachersForGame(String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
}