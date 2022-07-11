package net.qsef1256.dacobot.game.board.model;

/**
 * 보드 게임
 *
 * @param <T> Type of piece
 */
public interface BoardGame<T> {

    void place(int x, int y, T piece);

    String printBoard();
    
}
