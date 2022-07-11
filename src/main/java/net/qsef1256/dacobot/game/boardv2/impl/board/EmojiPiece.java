package net.qsef1256.dacobot.game.boardv2.impl.board;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.board.GamePiece;
import net.qsef1256.dacobot.game.paint.enums.Emoji;

public class EmojiPiece implements GamePiece {

    @Getter
    private final Emoji emoji;

    public EmojiPiece(Emoji emoji) {
        this.emoji = emoji;
    }

    public String getDisplay() {
        return emoji.getEmoji();
    }

}
