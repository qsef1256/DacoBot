package net.qsef1256.dacobot.game.board.model;

import net.dv8tion.jda.api.MessageBuilder;
import org.jetbrains.annotations.NotNull;

public interface GameUI {

    @NotNull MessageBuilder getUIMessage();

    @NotNull String getUIString();

}
