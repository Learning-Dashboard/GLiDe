package edu.upc.gessi.glidebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "team_player")
public class TeamPlayerEntity extends PlayerEntity {

    @Column(name = "project", nullable = false)
    private String project;

    @OneToMany(mappedBy = "teamPlayerEntity", fetch = FetchType.LAZY)
    private List<IndividualPlayerEntity> individualPlayerEntities;

}
