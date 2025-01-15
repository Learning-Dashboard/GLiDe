package edu.upc.gessi.glidegamificationengine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "teacher_user")
public class TeacherUserEntity extends UserEntity {

    @Column(name = "administrator", nullable = false)
    private Boolean administrator;

}
