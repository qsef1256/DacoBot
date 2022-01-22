package net.qsef1256.diabot.data;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "discord_user")
public class DiscordUser {
    @Id
    @Column(name = "discord_id", nullable = false)
    private Long id;

    @Column(name = "register_time")
    private Date registerTime;

    @Column(name = "status", nullable = false)
    @ColumnDefault("OK")
    private String status;

}
