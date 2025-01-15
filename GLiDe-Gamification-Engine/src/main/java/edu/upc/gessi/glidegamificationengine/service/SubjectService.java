package edu.upc.gessi.glidegamificationengine.service;

import edu.upc.gessi.glidegamificationengine.dto.SubjectDTO;

import javax.security.auth.Subject;
import java.util.List;

public interface SubjectService {

    SubjectDTO createSubject(String acronym, Integer code, String name, String school, String studies);

    List<SubjectDTO> getSubjects();

}
