package net.qsef1256.dacobot.game.boardv2.omok;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameTurn;
import net.qsef1256.dacobot.game.boardv2.impl.board.EmojiPiece;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;

public enum OmokTurn implements GameTurn {

    BLACK(new EmojiPiece(ColorEmoji.BLACK)),
    WHITE(new EmojiPiece(ColorEmoji.WHITE));

    @Getter
    private final EmojiPiece piece;

    OmokTurn(EmojiPiece piece) {
        this.piece = piece;
    }

    @Override
    public GameTurn getNextTurn() {
        return (this == BLACK) ? WHITE : BLACK;
    }

}
