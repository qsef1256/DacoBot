package net.qsef1256.dacobot.game.boardv2.omok;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.Game;
import net.qsef1256.dacobot.game.boardv2.api.user.GameTeam;
import net.qsef1256.dacobot.game.boardv2.api.user.GameUser;
import net.qsef1256.dacobot.game.boardv2.impl.user.GameRosterImpl;
import net.qsef1256.dacobot.game.boardv2.impl.user.GameTeamImpl;

import java.util.Set;

@Getter
public class OmokGame implements Game {

    private final OmokBoard board;
    private final GameRosterImpl roster;
    private final OmokUI UI;
    private final OmokCycle cycle;
    private final OmokParameter parameter;

    public OmokGame(GameUser black, GameUser white) {
        GameTeam blackTeam = new GameTeamImpl(OmokTurn.BLACK, Set.of(black));
        GameTeam whiteTeam = new GameTeamImpl(OmokTurn.WHITE, Set.of(white));

        Set<GameTeam> teams = Set.of(blackTeam, whiteTeam);

        roster = new GameRosterImpl(teams);
        board = new OmokBoard();
        cycle = new OmokCycle(this);
        UI = new OmokUI(this);
        parameter = new OmokParameter();
    }

    @Override
    public String toString() {
        return "OmokGame{board=%s, black=%s, white=%s, status=%s}".formatted(
                board.getDisplay(),
                roster.getTeam(OmokTurn.BLACK),
                roster.getFirstUser(OmokTurn.WHITE),
                getCycle().getStatus());
    }

}
