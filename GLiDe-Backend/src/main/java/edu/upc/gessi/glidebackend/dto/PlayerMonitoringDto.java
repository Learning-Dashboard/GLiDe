package edu.upc.gessi.glidebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerMonitoringDto {
    private Long id;
    private String playername;
    private Date startDate;
    private Date endDate;
    private String selectedMetrics;
    private String selectedHistoryMetrics;
    private String selectedBarMetrics;

}
