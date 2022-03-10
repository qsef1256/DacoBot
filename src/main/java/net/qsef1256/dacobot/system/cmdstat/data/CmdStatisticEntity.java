package net.qsef1256.dacobot.system.cmdstat.data;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
