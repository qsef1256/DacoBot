package net.qsef1256.dacobot.game.boardv2.omok;

import lombok.Getter;
import lombok.Setter;
import net.qsef1256.dacobot.game.boardv2.api.Game;
import net.qsef1256.dacobot.game.boardv2.api.action.GameAction;
import net.qsef1256.dacobot.game.boardv2.api.action.RemainGameAction;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameCycle;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameStatus;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameTurn;
import net.qsef1256.dacobot.util.CommonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

@Getter
public class OmokCycle implements GameCycle {

    private GameTurn turn;
    @Setter
    private GameStatus status;
    private final Game game;
    private final OmokBoard board;

    private final Queue<GameAction> actionQueue = new LinkedList<>();

    public OmokCycle(@NotNull Game game) {
        this.game = game;
        this.board = (OmokBoard) game.getBoard();
        init();
    }

    @Override
    public void init() {
        turn = OmokTurn.BLACK;
        status = OmokStatus.WAIT;
    }

    @Override
    public void run(@NotNull GameAction action) {
        if (isEnd()) throw new IllegalStateException("이미 게임이 끝났습니다.");
        if (action.canExecute()) throw new IllegalStateException("이 행동은 지금 실행할 수 없습니다.");

        action.execute();
        if (action instanceof RemainGameAction) {
            actionQueue.add(action);
        }
        turn = turn.getNextTurn();
    }

    public RemainGameAction getPrevAction() {
        return (RemainGameAction) actionQueue.peek();
    }

    public RemainGameAction removePrevAction() {
        return (RemainGameAction) actionQueue.remove();
    }

    @Override
    public boolean isEnd() {
        return CommonUtil.anySame(getStatus(),
                OmokStatus.WHITE_WIN,
                OmokStatus.BLACK_WIN,
                OmokStatus.BLACK_RESIGN,
                OmokStatus.WHITE_RESIGN,
                OmokStatus.DRAW);
    }

}
