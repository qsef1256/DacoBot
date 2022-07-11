package net.qsef1256.dacobot.game.boardv2.impl.board;

import lombok.Getter;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;

import java.awt.*;

public class ColorEmojiPiece extends EmojiPiece {

    @Getter
    private final ColorEmoji emoji;

    public ColorEmojiPiece(ColorEmoji emoji) {
        super(emoji);

        this.emoji = emoji;
    }

    public Color getColor() {
        return emoji.getColor();
    }
    
}
