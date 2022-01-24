package net.qsef1256.diabot.data;

import lombok.Getter;
import lombok.Setter;
import net.qsef1256.diabot.game.explosion.data.ExplosionCash;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "discord_user")
public class DiscordUserData {
    @Id
    @Column(name = "discord_id", nullable = false)
    private Long id;

    @Column(name = "register_time")
    private LocalDateTime registerTime;

    @Column(name = "status", nullable = false)
    @ColumnDefault(value = "'OK'")
    private String status;

    @Column(name = "last_attend_time")
    private LocalDateTime lastAttendTime;

    @Column(name = "attend_count")
    @ColumnDefault(value = "0")
    private Integer attendCount;

    @OneToOne(mappedBy = "discord_user", cascade = CascadeType.ALL)
    @JoinColumn(name = "discord_id", referencedColumnName = "discord_id")
    private ExplosionCash explosionCash;

}
