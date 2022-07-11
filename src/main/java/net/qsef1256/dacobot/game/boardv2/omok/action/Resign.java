package net.qsef1256.dacobot.game.boardv2.omok.action;

import net.qsef1256.dacobot.game.boardv2.api.Game;
import net.qsef1256.dacobot.game.boardv2.api.action.GameAction;
import net.qsef1256.dacobot.game.boardv2.omok.OmokStatus;
import net.qsef1256.dacobot.game.boardv2.omok.OmokTurn;

public class Resign extends GameAction {

    private final OmokTurn turn;

    public Resign(Game game, OmokTurn turn) {
        super(game);
        this.turn = turn;
    }

    @Override
    public void execute() {
        setStatus((turn == OmokTurn.WHITE) ? OmokStatus.WHITE_RESIGN : OmokStatus.BLACK_RESIGN);
    }

}
