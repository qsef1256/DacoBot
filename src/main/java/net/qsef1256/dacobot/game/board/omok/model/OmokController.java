package net.qsef1256.dacobot.game.board.omok.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import net.qsef1256.dacobot.module.request.model.RequestAPI;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.struct.NestedMap;
import net.qsef1256.dialib.util.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class OmokController { // TODO: cleanup or use another api

    @Setter(onMethod_ = {@Autowired})
    private JdaService jdaService;
    @Setter(onMethod_ = {@Autowired})
    private RequestAPI requestAPI;

    private final NestedMap<Long, String> omokMap = new NestedMap<>();

    public void requestGame(@NotNull MessageChannel channel,
                            @NotNull User user,
                            @NotNull User oppositeUser) {
        OmokRequest request = new OmokRequest(user.getIdLong(), oppositeUser.getIdLong(), channel);

        requestAPI.addRequest(request);
    }

    @Getter
    private enum OmokProcess {

        PREV("prev"),
        PREVIEW("preview"),
        CONFIRM("confirm"),
        CANCEL("cancel"),
        PLACE("place"),
        RESIGN("resign");

        private final String key;

        OmokProcess(String key) {
            this.key = key;
        }

    }

    public void createGame(@NotNull OmokRequest request) {
        long requesterId = request.getRequesterId();
        long receiverId = request.getReceiverId();
        if (getMessageId(requesterId) != null)
            throw new KeyAlreadyExistsException(jdaService.getNameAsTag(requesterId) + " 는 이미 진행 중인 오목 대국이 있습니다.");
        if (getMessageId(receiverId) != null)
            throw new KeyAlreadyExistsException(jdaService.getNameAsTag(receiverId) + " 는 이미 진행 중인 오목 대국이 있습니다.");

        OmokGame game = new OmokGame();
        request.getChannel().sendMessageEmbeds(getGameEmbed(receiverId, requesterId, game).build()).queue(callback -> {
            long messageId = callback.getIdLong();
            omokMap.put(messageId, "blackId", receiverId);
            omokMap.put(messageId, "whiteId", requesterId);
            omokMap.put(messageId, "game", game);
            omokMap.put(messageId, "channel", request.getChannel());
            omokMap.put(messageId, "isBlackTurn", true);
        });
    }

    public void prevStone(long userId) {
        processGame(userId, -1, -1, OmokProcess.PREV);
    }

    public void previewStone(long userId, int x, int y) {
        processGame(userId, x, y, OmokProcess.PREVIEW);
    }

    public void confirmStone(long userId) {
        processGame(userId, -1, -1, OmokProcess.CONFIRM);
    }

    public void cancelStone(long userId) {
        processGame(userId, -1, -1, OmokProcess.CANCEL);
    }

    public void forcePlaceStone(long userId, int x, int y) {
        processGame(userId, x, y, OmokProcess.PLACE);
    }

    public void endGame(long messageId) {
        log.info("endGame: %s".formatted(messageId));
        omokMap.remove(messageId);
    }

    public void resign(long userId) {
        processGame(userId, -1, -1, OmokProcess.RESIGN);
    }

    public void pullBoard(long userId, MessageChannel channel) {
        Long oldMessageId = getMessageId(userId);
        checkMessageId(oldMessageId, userId);

        OmokGame game = omokMap.get(oldMessageId, "game");
        Long blackId = omokMap.get(oldMessageId, "blackId");
        Long whiteId = omokMap.get(oldMessageId, "whiteId");
        if (!CommonUtil.anySame(blackId, whiteId, userId))
            throw new IllegalCallerException(jdaService.getNameAsTag(userId) + " 님, 당신의 게임이 아닌 것 같은데요...");

        omokMap.replace(oldMessageId, "channel", channel);
        channel.sendMessageEmbeds(getGameEmbed(blackId, whiteId, game).build()).queue(callback -> {
            Long newMessageId = callback.getIdLong();
            omokMap.putAll(newMessageId, omokMap.remove(oldMessageId));
        });
    }

    private void processGame(long userId, int x, int y, OmokProcess process) {
        Long messageId = getMessageId(userId);
        checkMessageId(messageId, userId);

        OmokGame game = omokMap.get(messageId, "game");
        Long blackId = omokMap.get(messageId, "blackId");
        Long whiteId = omokMap.get(messageId, "whiteId");
        MessageChannel channel = omokMap.get(messageId, "channel");
        if (CommonUtil.anyNull(game, blackId, whiteId, channel)) {
            log.warn("omokMap: %s".formatted(Objects.requireNonNull(omokMap.get(messageId)).values()));
            throw new IllegalStateException(jdaService.getNameAsTag(userId) + " 의 오목 게임을 로드하는 중 문제가 발생했습니다.");
        }
        if (!CommonUtil.anySame(blackId, whiteId, userId))
            throw new IllegalCallerException(jdaService.getNameAsTag(userId) + " 님, 당신의 게임이 아닌 것 같은데요...");

        ColorEmoji stone = getStone(userId, blackId, whiteId);

        switch (process) {
            case CONFIRM -> game.confirmStone(stone);
            case CANCEL -> game.resetPreview();
            case PLACE -> game.place(x, y, stone);
            case PREV -> game.prevStone(stone);
            case PREVIEW -> {
                game.previewStone(x, y, stone);
                channel.sendMessageEmbeds(getPreviewEmbed(x, y).build()).setActionRow(
                                Button.success("omok_confirm", "놓기"),
                                Button.danger("omok_cancel", "무르기"))
                        .queue();
            }
            case RESIGN -> game.resign(stone);
        }

        if (game.isEnd()) endGame(messageId);
        channel.editMessageEmbedsById(messageId, getGameEmbed(blackId, whiteId, game).build()).queue();
    }

    private void checkMessageId(Long messageId, long userId) {
        if (messageId == null)
            throw new NoSuchElementException(jdaService.getNameAsTag(userId) + " 의 오목 대국을 찾을 수 없습니다.");
    }

    @NotNull
    private EmbedBuilder getPreviewEmbed(int x, int y) {
        return new EmbedBuilder()
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .setColor(DiaColor.INFO)
                .addField("미리보기", "여기가 맞나요? x: %s, y: %s".formatted(x, y), false);
    }

    @NotNull
    private EmbedBuilder getGameEmbed(long blackId,
                                      long whiteId,
                                      @NotNull OmokGame game) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(OmokGame.BOARD.getColor())
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .addField("오목 게임", game.printBoard(), false)
                .addField(OmokGame.BLACK.getEmoji() + " 흑돌", jdaService.getNameAsTag(blackId), true)
                .addField(OmokGame.WHITE.getEmoji() + " 백돌", jdaService.getNameAsTag(whiteId), true)
                .addField("차례", game.isBlackTurn() ? OmokGame.BLACK.getEmoji() : OmokGame.WHITE.getEmoji(), true)
                .setFooter("경기 중에 채팅은 스크롤이 올라가니 적당히.");
        if (game.isEnd()) embedBuilder.addField(game.getStatus().getDisplay(), "게임 종료", false);
        return embedBuilder;
    }

    @Nullable
    private Long getMessageId(long userId) {
        AtomicReference<Long> messageId = new AtomicReference<>();
        omokMap.forEach((mainKey, subMap) -> subMap.forEach((subKey, data) -> {
            if ("blackId".equals(subKey) || "whiteId".equals(subKey) && String.valueOf(userId).equals(data)) {
                messageId.set(mainKey);
            }
        }));
        return messageId.get();
    }

    private ColorEmoji getStone(long userId, Long blackId, Long whiteId) {
        ColorEmoji stone = ColorEmoji.ORANGE;
        if (blackId == userId) stone = OmokGame.BLACK;
        if (whiteId == userId) stone = OmokGame.WHITE;
        return stone;
    }

}
