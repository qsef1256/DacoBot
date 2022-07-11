package net.qsef1256.dacobot.game.boardv2.api.lifecycle;

import net.qsef1256.dacobot.game.boardv2.api.board.GamePiece;

public interface GameTurn {

    GamePiece getPiece();

    GameTurn getNextTurn();

}
