package net.qsef1256.diabot.data;

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
public class CmdStatisticData {

    @Id
    private String commandName;
    @ColumnDefault(value = "0")
    private int useCount;
    @ColumnDefault(value = "0")
    private int todayUsed;
    private LocalDateTime lastUseTime;

}
