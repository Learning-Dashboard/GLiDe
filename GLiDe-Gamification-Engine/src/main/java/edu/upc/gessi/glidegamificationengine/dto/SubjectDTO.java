package edu.upc.gessi.glidegamificationengine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
    private String acronym;
    private Integer code;
    private String name;
    private String school;
    private String studies;
}
