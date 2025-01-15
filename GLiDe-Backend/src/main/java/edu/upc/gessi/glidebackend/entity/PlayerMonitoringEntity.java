package edu.upc.gessi.glidebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monitoring_dashboard")
public class PlayerMonitoringEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "selectedMetrics")
    private String selectedMetrics;

    @Column(name = "selectedHistoryMetrics")
    private String selectedHistoryMetrics;

    @Column(name = "selectedBarMetrics")
    private String selectedBarMetrics;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playername", referencedColumnName = "playername", nullable = false)
    private IndividualPlayerEntity individualPlayerEntity;
}