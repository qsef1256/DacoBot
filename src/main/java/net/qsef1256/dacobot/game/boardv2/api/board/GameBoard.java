package net.qsef1256.dacobot.game.boardv2.api.board;

import net.qsef1256.dacobot.game.boardv2.api.ui.display.StringDisplay;

public interface GameBoard extends Cloneable, StringDisplay {

    void reset();

    void setPiece(GameCoordinate coordinate, GamePiece piece);

    GamePiece getPiece(GameCoordinate coordinate);

}
