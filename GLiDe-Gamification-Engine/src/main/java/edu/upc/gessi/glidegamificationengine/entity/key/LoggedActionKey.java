package edu.upc.gessi.glidegamificationengine.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Embeddable
public class LoggedActionKey implements Serializable {

    @Column(name = "evaluable_action_id")
    private String evaluableActionId;

    @Column(name = "player_playername")
    private String playerPlayername;

    @Column(name = "timestamp")
    private Timestamp timestamp;

}
