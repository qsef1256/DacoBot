package net.qsef1256.dacobot.game.boardv2.impl.user;

import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameTurn;
import net.qsef1256.dacobot.game.boardv2.api.user.GameRoster;
import net.qsef1256.dacobot.game.boardv2.api.user.GameTeam;
import net.qsef1256.dacobot.game.boardv2.api.user.GameUser;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class GameRosterImpl implements GameRoster {

    private final Set<GameTeam> teamSet = new HashSet<>();

    public GameRosterImpl(@NotNull Set<GameTeam> teams) {
        teams.forEach(this::addTeam);
    }

    @Override
    public GameTeam getTeam(GameUser user) {
        return teamSet.stream().filter(team -> team.getUsers().contains(user)).findFirst().orElseThrow();
    }

    @Override
    public GameTeam getTeam(GameTurn turn) {
        return teamSet.stream().filter(team -> team.getTurn().equals(turn)).findFirst().orElseThrow();
    }

    @Override
    public void addTeam(GameTeam team) {
        teamSet.add(team);
    }

    @Override
    public void removeTeam(@NotNull GameTeam team) {
        teamSet.remove(team);
    }

    @Override
    public Set<GameTeam> getAllTeams() {
        return teamSet;
    }

    @Override
    public User getFirstUser(@NotNull GameTeam team) {
        return getFirstUser(team.getTurn());
    }

    @Override
    public User getFirstUser(GameTurn turn) {
        return getTeam(turn).getUsers().stream().findFirst().orElseThrow().getUser();
    }

    @Override
    public String toString() {
        return "GameRosterImpl{" + "teamSet=" + teamSet + '}';
    }

}
