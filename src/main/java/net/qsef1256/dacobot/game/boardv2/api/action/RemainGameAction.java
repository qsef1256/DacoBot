package net.qsef1256.dacobot.game.boardv2.api.action;

import net.qsef1256.dacobot.game.boardv2.api.Game;

public abstract class RemainGameAction extends GameAction {

    protected RemainGameAction(Game game) {
        super(game);
    }

    public abstract void reset();

}
