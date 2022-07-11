package net.qsef1256.dacobot.service.openapi.corona;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import net.qsef1256.dacobot.util.gson.GsonExclude;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Table(name = "corona_data")
public class CoronaEntity {

    @Id
    @GsonExclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private long deathCnt;
    private long decideCnt;
    private long addDeath;
    private long addDecide;
    private LocalDateTime updateTime;

    protected CoronaEntity() {
    }

    public CoronaEntity(long deathCnt, long decideCnt, long addDeath, long addDecide, LocalDateTime updateTime) {
        this.deathCnt = deathCnt;
        this.decideCnt = decideCnt;
        this.addDeath = addDeath;
        this.addDecide = addDecide;
        this.updateTime = updateTime;
    }

}
