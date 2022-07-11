package net.qsef1256.dacobot.game.boardv2.impl.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.board.GameCoordinate;

@Getter
@AllArgsConstructor
public class XYCoordinate implements GameCoordinate {

    private int x;
    private int y;

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }

}
