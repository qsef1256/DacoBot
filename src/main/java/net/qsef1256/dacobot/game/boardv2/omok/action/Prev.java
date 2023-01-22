package net.qsef1256.dacobot.game.boardv2.omok.action;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.Game;
import net.qsef1256.dacobot.game.boardv2.api.action.RemainGameAction;
import net.qsef1256.dacobot.game.boardv2.impl.board.EmojiPiece;
import net.qsef1256.dacobot.game.boardv2.impl.board.GridBoard;
import net.qsef1256.dacobot.game.boardv2.impl.board.XYCoordinate;
import net.qsef1256.dacobot.game.boardv2.omok.OmokStatus;

class Prev extends RemainGameAction {

    @Getter
    private XYCoordinate coordinate;
    @Getter
    private EmojiPiece color;

    private final GridBoard board = (GridBoard) getBoard();

    public Prev(Game game) {
        super(game);
    }

    @Override
    public void checkExecute() {
        checkMatchStatus(OmokStatus.WAIT);
    }

    @Override
    public void execute() {
        if (coordinate == null)
            throw new IllegalStateException("저장된 로그가 없습니다.");

        color = (EmojiPiece) board.getPiece(coordinate);
        board.setPiece(coordinate, color);
        setStatus(OmokStatus.PREV);
    }

    @Override
    public void reset() {
        board.setPiece(coordinate, color);

        setStatus(OmokStatus.WAIT);
        color = null;
    }

}
