package net.qsef1256.dacobot.game.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.User;

import java.util.Set;

@Getter
@AllArgsConstructor
public class GameTeam {

    private GamePiece piece;
    
    private Set<User> users;

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

}
