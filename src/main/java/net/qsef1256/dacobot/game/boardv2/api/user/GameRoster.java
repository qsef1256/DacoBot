package net.qsef1256.dacobot.game.boardv2.api.user;

import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameTurn;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface GameRoster {

    GameTeam getTeam(GameTurn turn);

    GameTeam getTeam(GameUser user);

    void addTeam(GameTeam team);

    void removeTeam(@NotNull GameTeam team);

    Set<GameTeam> getAllTeams();

    User getFirstUser(@NotNull GameTeam team);

    User getFirstUser(GameTurn turn);

}
