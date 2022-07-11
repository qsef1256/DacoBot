package net.qsef1256.dacobot.game.boardv2.omok.action;

import net.qsef1256.dacobot.game.boardv2.api.action.GameAction;
import net.qsef1256.dacobot.game.boardv2.impl.board.XYCoordinate;
import net.qsef1256.dacobot.game.boardv2.omok.OmokBoard;
import net.qsef1256.dacobot.game.boardv2.omok.OmokGame;
import net.qsef1256.dacobot.game.boardv2.omok.OmokStatus;
import net.qsef1256.dacobot.game.boardv2.omok.OmokTurn;

public class Win extends GameAction {

    private final XYCoordinate lastPlaced;

    public Win(OmokGame game, XYCoordinate lastPlaced) {
        super(game);

        this.lastPlaced = lastPlaced;
    }

    @Override
    public void checkExecute() {
        OmokBoard board = (OmokBoard) getBoard();

        if (!board.isWin(lastPlaced, getTurn())) {
            throw new IllegalStateException(getTurn() + "은 아직 이기지 않았습니다.");
        }
    }

    @Override
    public void execute() {
        setStatus((getTurn() == OmokTurn.WHITE) ? OmokStatus.WHITE_WIN : OmokStatus.BLACK_WIN);
    }

}
