package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.entity.key.LoggedActionKey;
import edu.upc.gessi.glidegamificationengine.type.ActionCategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "logged_action")
public abstract class LoggedActionEntity {

    @EmbeddedId
    private LoggedActionKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("evaluableActionId")
    @JoinColumn(name = "evaluable_action_id")
    private EvaluableActionEntity evaluableActionEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playerPlayername")
    @JoinColumn(name = "player_playername")
    private PlayerEntity playerEntity;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionCategoryType type;

}
