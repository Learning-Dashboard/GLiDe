package edu.upc.gessi.glidebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "individual_player")
public class IndividualPlayerEntity extends PlayerEntity {

    @Column(name = "role")
    private String role;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_player_playername", nullable = false)
    private TeamPlayerEntity teamPlayerEntity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_user_username", nullable = false)
    private StudentUserEntity studentUserEntity;

}
