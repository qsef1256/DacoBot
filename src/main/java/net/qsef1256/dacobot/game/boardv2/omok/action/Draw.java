package net.qsef1256.dacobot.game.boardv2.omok.action;

import net.qsef1256.dacobot.game.board.omok.model.OmokGame;
import net.qsef1256.dacobot.game.boardv2.api.Game;
import net.qsef1256.dacobot.game.boardv2.api.action.GameAction;
import net.qsef1256.dacobot.game.boardv2.impl.board.GridBoard;
import net.qsef1256.dacobot.game.boardv2.omok.OmokStatus;

import java.util.concurrent.atomic.AtomicBoolean;

class Draw extends GameAction {

    private final GridBoard board = (GridBoard) getBoard();

    public Draw(Game game) {
        super(game);
    }

    @Override
    public void execute() {
        if (isDraw()) setStatus(OmokStatus.DRAW);
    }

    private boolean isDraw() {
        AtomicBoolean anyEmpty = new AtomicBoolean(false);
        board.getGrid().getPixels().forEach(pixel -> {
            if (pixel == OmokGame.BOARD) anyEmpty.set(true);
        });
        return !anyEmpty.get();
    }

}
