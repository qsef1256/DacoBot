package net.qsef1256.dacobot.game.boardv2.omok;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.qsef1256.dacobot.game.boardv2.api.ui.MessageUI;
import net.qsef1256.dacobot.game.boardv2.api.ui.TextUI;
import net.qsef1256.dacobot.game.boardv2.impl.board.GridBoard;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.util.JDAUtil;

import static net.qsef1256.dacobot.game.boardv2.omok.OmokTurn.BLACK;
import static net.qsef1256.dacobot.game.boardv2.omok.OmokTurn.WHITE;

public class OmokUI implements MessageUI, TextUI {

    private final OmokGame game;

    public OmokUI(OmokGame game) {
        this.game = game;
    }

    @Override
    public MessageBuilder getMessageUI() {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(GridBoard.BOARD.getEmoji().getColor())
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .addField("오목 게임", game.getBoard().getDisplay(), false) // 이 코드는 망했어
                .addField(BLACK.getPiece() + " 흑돌", JDAUtil.getNameAsTag(game.getRoster().getFirstUser(BLACK).getIdLong()), true)
                .addField(WHITE.getPiece() + " 백돌", JDAUtil.getNameAsTag(game.getRoster().getFirstUser(WHITE).getIdLong()), true)
                .addField("차례", game.getCycle().getTurn().getPiece().getDisplay(), true)
                .setFooter("경기 중에 채팅은 스크롤이 올라가니 적당히.");
        embedBuilder.addField("게임 상태", game.getCycle().getStatus().getDisplay(), false);
        return new MessageBuilder().append(embedBuilder);
    }

    @Override
    public String getTextUI() {
        return null; // TODO
    }

}
