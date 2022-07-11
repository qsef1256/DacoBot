package net.qsef1256.dacobot.game.boardv2.api.user;

import net.qsef1256.dacobot.game.boardv2.api.board.GamePiece;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameTurn;

import java.util.Set;

public interface GameTeam {

    GameTurn getTurn();

    default GamePiece getPiece() {
        return getTurn().getPiece();
    }

    Set<GameUser> getUsers();

}
