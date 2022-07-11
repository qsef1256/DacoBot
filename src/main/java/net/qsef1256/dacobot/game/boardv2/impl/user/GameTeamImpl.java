package net.qsef1256.dacobot.game.boardv2.impl.user;

import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameTurn;
import net.qsef1256.dacobot.game.boardv2.api.user.GameTeam;
import net.qsef1256.dacobot.game.boardv2.api.user.GameUser;

import java.util.Set;

public class GameTeamImpl implements GameTeam {

    private final GameTurn turn;
    private final Set<GameUser> users;

    public GameTeamImpl(GameTurn turn, Set<GameUser> users) {
        this.turn = turn;
        this.users = users;
    }

    @Override
    public GameTurn getTurn() {
        return turn;
    }

    @Override
    public Set<GameUser> getUsers() {
        return users;
    }

}
