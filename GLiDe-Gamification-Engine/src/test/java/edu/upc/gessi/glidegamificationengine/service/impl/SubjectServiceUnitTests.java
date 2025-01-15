package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.SubjectDTO;
import edu.upc.gessi.glidegamificationengine.entity.SubjectEntity;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

public class SubjectServiceUnitTests {

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Mock
    private SubjectRepository subjectRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSubjects() {
        SubjectEntity entity1 = new SubjectEntity("PES", "Projecte Enginyeria Software", 101, "FIB", "UPC");
        SubjectEntity entity2 = new SubjectEntity("ASW", "Aplicacions i Serveix Web", 102, "FIB", "UPC");
        when(subjectRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        List<SubjectDTO> subjects = subjectService.getSubjects();

        assertEquals(2, subjects.size());
        assertEquals("PES", subjects.getFirst().getAcronym());
        assertEquals("Projecte Enginyeria Software", subjects.getFirst().getName());
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void testCreateSubjectSuccess() {
        String acronym = "PES";
        int code = 101;
        String name = "Projecte Enginyeria Software";
        String school = "FIB";
        String studies = "GEI";

        SubjectEntity entity = new SubjectEntity(acronym, name, code, school, studies);
        when(subjectRepository.existsById(acronym)).thenReturn(false);
        when(subjectRepository.save(any(SubjectEntity.class))).thenReturn(entity);

        SubjectDTO result = subjectService.createSubject(acronym, code, name, school, studies);

        assertNotNull(result);
        assertEquals(acronym, result.getAcronym());
        assertEquals(name, result.getName());
        verify(subjectRepository, times(1)).existsById(acronym);
        verify(subjectRepository, times(1)).save(any(SubjectEntity.class));
    }

    @Test
    void testCreateSubjectBlankAcronym() {
        String acronym = "";
        int code = 101;
        String name = "Projecte Enginyeria Software";
        String school = "FIB";
        String studies = "GEI";

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> subjectService.createSubject(acronym, code, name, school, studies));
        assertEquals("Subject acronym cannot be blank.", exception.getMessage());
        verify(subjectRepository, never()).save(any(SubjectEntity.class));
    }

    @Test
    void testCreateSubjectAlreadyExists() {
        String acronym = "PES";
        int code = 101;
        String name = "Projecte Enginyeria Software";
        String school = "FIB";
        String studies = "GEI";

        when(subjectRepository.existsById(acronym)).thenReturn(true);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> subjectService.createSubject(acronym, code, name, school, studies));
        assertEquals("Subject already exists.", exception.getMessage());
        verify(subjectRepository, times(1)).existsById(acronym);
        verify(subjectRepository, never()).save(any(SubjectEntity.class));
    }

    @Test
    void testCreateSubjectDuplicateCodeOrName() {
        String acronym = "PES";
        int code = 101;
        String name = "Projecte Enginyeria Software";
        String school = "FIB";
        String studies = "GEI";

        when(subjectRepository.existsById(acronym)).thenReturn(false);
        when(subjectRepository.save(any(SubjectEntity.class))).thenThrow(new ConstraintViolationException("Subject with the same code or name exists."));

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> subjectService.createSubject(acronym, code, name, school, studies));
        assertEquals("Subject with the same code or name exists.", exception.getMessage());
        verify(subjectRepository, times(1)).existsById(acronym);
        verify(subjectRepository, times(1)).save(any(SubjectEntity.class));
    }
}
