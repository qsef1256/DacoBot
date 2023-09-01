package net.qsef1256.dacobot.service.admin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import net.qsef1256.dacobot.service.account.data.UserEntity;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "punish_data")
public class PunishEntity {

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

    public enum PunishType {
        WARN, BAN
    }

}
