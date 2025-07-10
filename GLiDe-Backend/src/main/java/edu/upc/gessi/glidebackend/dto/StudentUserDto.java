package edu.upc.gessi.glidebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentUserDto {
    private String username;
    private String learningdashboardUsername;
    private String githubUsername;
    private String taigaUsername;
}
