package net.qsef1256.dacobot.module.cmdstat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cmd_statistic")
public class CmdStatisticEntity {

    @Id
    private String commandName;

    @ColumnDefault(value = "0")
    private int useCount = 0;

    @ColumnDefault(value = "0")
    private int todayUsed = 0;
    private LocalDateTime lastUseTime;

}
