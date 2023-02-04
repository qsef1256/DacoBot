package net.qsef1256.dacobot.game.board.model;

import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

public interface GameUI {

    @NotNull MessageCreateBuilder getUIMessage();

    @NotNull String getUIString();

}
