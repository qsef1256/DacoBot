package net.qsef1256.dacobot.game.boardv2.omok.action;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.action.RemainGameAction;
import net.qsef1256.dacobot.game.boardv2.impl.board.EmojiPiece;
import net.qsef1256.dacobot.game.boardv2.impl.board.GridBoard;
import net.qsef1256.dacobot.game.boardv2.impl.board.XYCoordinate;
import net.qsef1256.dacobot.game.boardv2.omok.OmokGame;
import net.qsef1256.dacobot.game.boardv2.omok.OmokStatus;

class Preview extends RemainGameAction {

    @Getter
    private final XYCoordinate coordinate;
    @Getter
    private EmojiPiece color;

    private final GridBoard board = (GridBoard) getBoard();

    public Preview(OmokGame game, XYCoordinate coordinate) {
        super(game);
        this.coordinate = coordinate;
    }

    @Override
    public void checkExecute() {
        checkMatchStatus(OmokStatus.WAIT);
    }

    @Override
    public void execute() {
        if (!board.canSet(coordinate)) throw new IllegalArgumentException(coordinate + " 위치에는 놓을 수 없습니다.");

        color = (EmojiPiece) board.getPiece(coordinate);
        board.setPiece(coordinate, GridBoard.PREVIEW);
        setStatus(OmokStatus.PREVIEW);
    }

    public void confirm() {
        new Place((OmokGame) getGame(), coordinate);
    }

    @Override
    public void reset() {
        board.setPiece(coordinate, color);

        setStatus(OmokStatus.WAIT);
        color = null;
    }

}
