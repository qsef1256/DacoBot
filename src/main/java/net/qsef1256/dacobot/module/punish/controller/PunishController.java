package net.qsef1256.dacobot.module.punish.controller;

import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.module.punish.entity.PunishRepository;
import net.qsef1256.dacobot.module.punish.entity.PunishType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class PunishController {

    private final PunishRepository repository;

    public PunishController(@NotNull PunishRepository repository) {
        this.repository = repository;
    }

    public void punish(@NotNull User user,
                       @NotNull PunishType type,
                       @NotNull String reason,
                       @NotNull Duration duration) {
        // TODO: save and activate punish
    }

}
