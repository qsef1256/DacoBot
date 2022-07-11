package net.qsef1256.dacobot.game.board.ability;

/**
 * 강제로 해결될 수 있는 보드게임
 */
public interface Solvable {

    /**
     * 게임을 강제로 해결합니다.
     *
     * @return is solvable?
     */
    boolean solve();

}
