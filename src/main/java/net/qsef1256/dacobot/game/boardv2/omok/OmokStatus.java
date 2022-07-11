package net.qsef1256.dacobot.game.boardv2.omok;

import lombok.Getter;
import net.qsef1256.dacobot.game.boardv2.api.lifecycle.GameStatus;

public enum OmokStatus implements GameStatus {

    WAIT("착수 대기 중"),
    WHITE_WIN(OmokTurn.WHITE.getPiece().getEmoji() + " 승리"),
    BLACK_WIN(OmokTurn.BLACK.getPiece().getEmoji() + " 승리"),
    WHITE_RESIGN(OmokTurn.WHITE.getPiece().getEmoji() + " 기권"),
    BLACK_RESIGN(OmokTurn.BLACK.getPiece().getEmoji() + " 기권"),
    DRAW("무승부"),
    PREVIEW("미리보기"),
    PREV("로그 확인");

    @Getter
    private final String display;

    OmokStatus(String display) {
        this.display = display;
    }

}
