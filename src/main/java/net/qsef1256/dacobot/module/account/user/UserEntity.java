package net.qsef1256.dacobot.module.account.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "discord_user")
public class UserEntity implements Serializable {

    @Id
    @Column(name = "discord_id", nullable = false)
    private Long discordId;

    private LocalDateTime registerTime = LocalDateTime.now();

    // TODO: separation required (maybe)
    private LocalDateTime lastAttendTime;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer attendCount = 0;

}
