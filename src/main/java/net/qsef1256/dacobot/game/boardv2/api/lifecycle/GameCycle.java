package net.qsef1256.dacobot.game.boardv2.api.lifecycle;

import net.qsef1256.dacobot.game.boardv2.api.action.GameAction;
import org.jetbrains.annotations.NotNull;

public interface GameCycle {

    GameStatus getStatus();

    void setStatus(GameStatus status);

    GameTurn getTurn();

    void init();

    void run(@NotNull GameAction action);

    boolean isEnd();

}
