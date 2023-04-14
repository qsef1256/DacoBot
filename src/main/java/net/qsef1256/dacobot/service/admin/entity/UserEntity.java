package net.qsef1256.dacobot.service.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class UserEntity {

    @Id
    private long userId;

    private UserStatus status;
    private int warnCount;
    private LocalDateTime banTime;

}
