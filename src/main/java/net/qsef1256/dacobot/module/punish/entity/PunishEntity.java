package net.qsef1256.dacobot.module.punish.entity;

import jakarta.persistence.*;
import lombok.Getter;
import net.qsef1256.dacobot.module.account.user.UserEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "punish_data")
public class PunishEntity implements Serializable {

    @Id
    private long id;

    @OneToOne
    @JoinColumn(name = "user")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private PunishType type;
    private String reason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
