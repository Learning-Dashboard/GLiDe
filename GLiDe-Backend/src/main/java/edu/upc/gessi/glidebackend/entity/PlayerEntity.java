package edu.upc.gessi.glidebackend.entity;

import edu.upc.gessi.glidebackend.type.PlayerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "player")
public abstract class PlayerEntity {

    @Id
    @Column(name = "playername")
    private String playername;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerType type;

}
