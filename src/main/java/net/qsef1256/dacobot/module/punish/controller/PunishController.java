package net.qsef1256.dacobot.module.punish.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.module.punish.entity.PunishEntity;
import net.qsef1256.dacobot.module.punish.entity.PunishType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@NoArgsConstructor
public class PunishController {

    @Getter
    private static final PunishController instance = new PunishController();

    private final DaoCommonJpa<PunishEntity, Long> punishDao = new DaoCommonJpaImpl<>(PunishEntity.class);

    public void punish(@NotNull User user,
                       @NotNull PunishType type,
                       @NotNull String reason,
                       @NotNull Duration duration) {
        punishDao.open();

        // TODO: save and activate punish
    }

}
