package net.qsef1256.dacobot.game.board.model;

/**
 * 기권, 승패 등을 다룹니다.
 */
public interface Game {

    boolean playerExist(long userId);

    void resign(long userId);

    void win(long userId);

    boolean isEnd();

    GameUI getUI();

    @Override
    String toString();

}
