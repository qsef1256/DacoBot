package net.qsef1256.dacobot.game.boardv2.omok.action;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.action.GameAction;
import net.qsef1256.dacobot.game.boardv2.impl.board.GridBoard;
import net.qsef1256.dacobot.game.boardv2.impl.board.XYCoordinate;
import net.qsef1256.dacobot.game.boardv2.omok.OmokGame;
import net.qsef1256.dacobot.game.boardv2.omok.OmokStatus;

public class Place extends GameAction {

    private final GridBoard board = (GridBoard) getBoard();
    @Getter
    private final XYCoordinate coordinate;

    public Place(OmokGame game, XYCoordinate coordinate) {
        super(game);

        this.coordinate = coordinate;
    }

    @Override
    public void checkExecute() {
        checkMatchStatus(OmokStatus.WAIT);
    }

    @Override
    public void execute() {
        board.setPiece(coordinate, getTurn().getPiece());

        new Win((OmokGame) getGame(), coordinate).executeIfCan();
    }

}
