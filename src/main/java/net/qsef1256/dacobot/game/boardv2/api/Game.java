package net.qsef1256.dacobot.game.boardv2.api;

import net.qsef1256.dacobot.game.boardv2.api.board.GameBoard;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameCycle;
import net.qsef1256.dacobot.game.boardv2.api.ui.GameUI;
import net.qsef1256.dacobot.game.boardv2.impl.user.GameRosterImpl;

public interface Game {

    GameUI getUI();

    GameCycle getCycle();

    GameRosterImpl getRoster();

    GameBoard getBoard();

    GameParameter getParameter();

}
