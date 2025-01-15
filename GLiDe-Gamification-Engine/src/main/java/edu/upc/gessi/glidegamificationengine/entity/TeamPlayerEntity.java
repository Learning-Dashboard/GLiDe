package edu.upc.gessi.glidegamificationengine.entity;

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

    @Lob
    @Column(name = "logo", nullable = false)
    private String logo;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private ProjectEntity projectEntity;

    @OneToMany(mappedBy = "teamPlayerEntity", fetch = FetchType.LAZY)
    private List<IndividualPlayerEntity> individualPlayerEntities;

    @Override
    public void updateLevel() {
        super.setLevel(projectEntity.getGameGroupEntity().getGameEntity().calculateLevel(super.getPoints()));
    }
}
