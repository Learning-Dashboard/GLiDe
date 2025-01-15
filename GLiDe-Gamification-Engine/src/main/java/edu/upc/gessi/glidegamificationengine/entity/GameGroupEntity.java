package edu.upc.gessi.glidegamificationengine.entity;

import edu.upc.gessi.glidegamificationengine.entity.key.GameGroupKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game_group")
public class GameGroupEntity {

    @EmbeddedId
    private GameGroupKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gameId")
    @JoinColumns({
            @JoinColumn(name = "game_subject_acronym", referencedColumnName = "subject_acronym"),
            @JoinColumn(name = "game_course", referencedColumnName = "course"),
            @JoinColumn(name = "game_period", referencedColumnName = "period")})
    private GameEntity gameEntity;

    @OneToMany(mappedBy = "gameGroupEntity", fetch = FetchType.LAZY)
    private List<ProjectEntity> projectEntities;

    public Map<String, String> getTeamPlayerLogos() {
        Map<String, String> teamPlayerLogos = new HashMap<>();
        for (ProjectEntity projectEntity : projectEntities) {
            teamPlayerLogos.put(projectEntity.getTeamPlayerEntity().getPlayername(), projectEntity.getTeamPlayerEntity().getLogo());
        }
        return teamPlayerLogos;
    }

    public Map<String, String> getIndividualPlayerAvatars() {
        Map<String, String> individualPlayerAvatars = new HashMap<>();
        for (ProjectEntity projectEntity : projectEntities) {
            for (IndividualPlayerEntity individualPlayerEntity : projectEntity.getTeamPlayerEntity().getIndividualPlayerEntities()) {
                individualPlayerAvatars.put(individualPlayerEntity.getPlayername(), individualPlayerEntity.getAvatar());
            }
        }
        return individualPlayerAvatars;
    }
}
