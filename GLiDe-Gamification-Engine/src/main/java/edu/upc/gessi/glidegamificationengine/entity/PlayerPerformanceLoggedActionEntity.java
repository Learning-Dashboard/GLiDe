package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.entity.key.LoggedActionKey;
import edu.upc.gessi.glidegamificationengine.type.ActionCategoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "player_performance_logged_action")
public class PlayerPerformanceLoggedActionEntity extends LoggedActionEntity {

    @Column(name = "value", nullable = false)
    private Float value;

    public PlayerPerformanceLoggedActionEntity(LoggedActionKey id, EvaluableActionEntity evaluableActionEntity, PlayerEntity playerEntity, Float value) {
        super(id, evaluableActionEntity, playerEntity, ActionCategoryType.PlayerPerformance);
        this.value = value;
    }

}
