package net.qsef1256.dacobot.game.boardv2.impl.board;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.board.GamePiece;

public class StringPiece implements GamePiece {

    @Getter
    private final String piece;

    public StringPiece(String piece) {
        this.piece = piece;
    }

    @Override
    public String getDisplay() {
        return piece;
    }

}
