package net.qsef1256.dacobot.game.board.sudoku.model;

import lombok.Getter;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.game.board.model.Game;
import net.qsef1256.dacobot.game.board.model.GameUI;
import org.jetbrains.annotations.NotNull;

public class SudokuInterface implements Game {

    @Getter
    private SudokuGame sudokuGame;
    @Getter
    private final User user;

    public SudokuInterface(User user) {
        this.user = user;

        sudokuGame = new SudokuGame(35);
    }

    public @NotNull MessageBuilder getUIMessage() {
        return new MessageBuilder()
                .append("""
                        **:checkered_flag: 스도쿠**
                                        
                        """)
                .append(sudokuGame.printBoard())
                .append("사용자: ")
                .append(user.getName());
    }

    public @NotNull String getUIString() {
        return "스도쿠 게임\n" + sudokuGame.getBoard().printSudoku();
    }

    @Override
    public boolean playerExist(long userId) {
        return user.getIdLong() == userId;
    }

    @Override
    public void resign(long userId) {
        sudokuGame = null;
        sudokuGame = new SudokuGame(35);
    }

    @Override
    public void win(long userId) {
        // TODO
    }

    @Override
    public boolean isEnd() {
        return sudokuGame.isEnd();
    }

    @Override
    public GameUI getUI() {
        return null;
    }

    public void place(int x, int y, Byte piece) {
        sudokuGame.place(x, y, piece);

        if (sudokuGame.isEnd()) win(user.getIdLong());
    }

}
