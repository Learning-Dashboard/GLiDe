package edu.upc.gessi.glidebackend.service;

import edu.upc.gessi.glidebackend.dto.IndividualPlayerDto;
import edu.upc.gessi.glidebackend.dto.StudentUserDto;
import edu.upc.gessi.glidebackend.dto.StudentNicknameDto;

import java.util.List;

public interface StudentService {
    StudentUserDto getStudent(String idToken);
    List<IndividualPlayerDto> getStudentPlayers(String idToken);
    List<IndividualPlayerDto> getStudentsByGame(String subjectAcronym, Integer course, String period);
    void updateNickname(String idToken, String nickname);
    List<StudentNicknameDto> getAllStudentNicknames();
}
