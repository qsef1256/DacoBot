package net.qsef1256.dacobot.game.boardv2.omok.action;

import net.qsef1256.dacobot.game.boardv2.api.Game;
import net.qsef1256.dacobot.game.boardv2.api.action.GameAction;
import net.qsef1256.dacobot.game.boardv2.omok.OmokStatus;

public class Confirm extends GameAction {

    public Confirm(Game game) {
        super(game);
    }

    @Override
    public void checkExecute() {
        checkMatchStatus(OmokStatus.PREVIEW);
    }

    @Override
    public void execute() {
        // TODO
    }

}
