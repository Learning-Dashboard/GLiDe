package edu.upc.gessi.glidebackend.service;

import edu.upc.gessi.glidebackend.dto.IndividualPlayerDto;
import edu.upc.gessi.glidebackend.dto.StudentUserDto;

import java.util.List;

public interface StudentService {
    StudentUserDto getStudent(String idToken);
    List<IndividualPlayerDto> getStudentPlayers(String idToken);
}
