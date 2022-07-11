package net.qsef1256.dacobot.game.boardv2.api.action;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.Game;
import net.qsef1256.dacobot.game.boardv2.api.GameParameter;
import net.qsef1256.dacobot.game.boardv2.api.board.GameBoard;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameCycle;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameStatus;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameTurn;
import net.qsef1256.dacobot.util.CommonUtil;

public abstract class GameAction {

    @Getter
    private final Game game;

    protected GameAction(Game game) {
        this.game = game;
    }

    protected void checkExecute() {
    }

    public abstract void execute();

    public void executeIfCan() {
        if (canExecute()) execute();
    }

    public boolean canExecute() {
        try {
            checkExecute();
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }

    protected void checkMatchStatus(GameStatus... statuses) {
        if (CommonUtil.anySame(getStatus(), statuses)) return;

        String[] displays = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            displays[i] = getStatus().getDisplay();
        }
        throw new IllegalStateException("게임이 %s 상태가 아닙니다. 현재 상태: %s".formatted(String.join(", ", displays), getStatus().getDisplay()));
    }

    protected GameStatus getStatus() {
        return game.getCycle().getStatus();
    }

    protected void setStatus(GameStatus status) {
        game.getCycle().setStatus(status);
    }

    protected GameBoard getBoard() {
        return game.getBoard();
    }

    protected GameTurn getTurn() {
        return game.getCycle().getTurn();
    }

    protected GameCycle getCycle() {
        return game.getCycle();
    }

    protected GameParameter getParameter() {
        return game.getParameter();
    }

}
