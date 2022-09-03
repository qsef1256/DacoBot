package net.qsef1256.dacobot.game.board.sudoku.model;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.game.board.model.GameHost;
import net.qsef1256.dacobot.game.board.model.GameUI;
import net.qsef1256.dacobot.service.key.SingleUserKey;
import net.qsef1256.dacobot.service.key.UserKey;

import java.util.Set;

@Slf4j
public class SudokuHost extends GameHost<SudokuInterface> {

    public SudokuHost(User user) {
        super(new SingleUserKey("sudoku", user));
    }

    @Override
    public void init() {
        setGame(new SudokuInterface(((SingleUserKey) getKey()).getUser()));
    }

    @Override
    public boolean isEnd() {
        return getGame().isEnd();
    }

    @Override
    public GameUI getUI() {
        return getGame().getUI();
    }

    public Set<User> getUsers() {
        return ((UserKey) getKey()).getUsers();
    }

}
