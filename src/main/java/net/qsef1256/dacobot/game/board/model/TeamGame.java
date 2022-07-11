package net.qsef1256.dacobot.game.board.model;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TeamGame implements Game {

    private final Set<GameTeam> teamMap;

    protected TeamGame(Set<GameTeam> teamMap) {
        this.teamMap = teamMap;
    }

    public Set<GameTeam> getTeams() {
        return teamMap;
    }

    @Override
    public boolean playerExist(long userId) {
        AtomicBoolean playerExist = new AtomicBoolean(false);

        teamMap.forEach(team -> team.getUsers().forEach(users -> {
            if (users.getIdLong() == userId) playerExist.set(true);
        }));

        return playerExist.get();
    }

    @Override
    public String toString() {
        return "%s@%s [users = %s, game = %s]".formatted(getClass().getSimpleName(), hashCode(), teamMap.stream().map(GameTeam::getUsers), getUI().getUIString());
    }

}
