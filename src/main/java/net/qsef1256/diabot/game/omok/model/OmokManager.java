package net.qsef1256.diabot.game.omok.model;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.enums.DiaImage;
import net.qsef1256.diabot.enums.DiaInfo;
import net.qsef1256.diabot.game.paint.enums.PixelColor;
import net.qsef1256.diabot.struct.NestedMap;
import net.qsef1256.diabot.system.request.model.RequestManager;
import net.qsef1256.diabot.util.CommonUtil;
import net.qsef1256.diabot.util.DiscordUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static net.qsef1256.diabot.DiaBot.logger;

public class OmokManager {

    private static final NestedMap<Long, String> omokMap = new NestedMap<>();

    public static void requestGame(MessageChannel channel, @NotNull User user, @NotNull User oppositeUser) {
        OmokRequest request = new OmokRequest(user.getIdLong(), oppositeUser.getIdLong(), channel);

        RequestManager.addRequest(request);
    }

    public static void createGame(@NotNull OmokRequest request) {
        logger.info("OmokManager#createGame> " + request.getTitle() + " " + request.getRequesterId() + " " + request.getReceiverId());
        long requesterId = request.getRequesterId();
        long receiverId = request.getReceiverId();
        if (getMessageId(requesterId) != null)
            throw new KeyAlreadyExistsException(DiscordUtil.getNameAsTag(requesterId) + " 는 이미 진행 중인 오목 대국이 있습니다.");
        if (getMessageId(receiverId) != null)
            throw new KeyAlreadyExistsException(DiscordUtil.getNameAsTag(receiverId) + " 는 이미 진행 중인 오목 대국이 있습니다.");

        OmokGame game = new OmokGame();
        request.getChannel().sendMessageEmbeds(getGameEmbed(receiverId, requesterId, game)
                .build()).queue(callback -> {
            long messageId = callback.getIdLong();
            omokMap.put(messageId, "blackId", receiverId);
            omokMap.put(messageId, "whiteId", requesterId);
            omokMap.put(messageId, "game", game);
            omokMap.put(messageId, "channel", request.getChannel());
            omokMap.put(messageId, "isBlackTurn", true);
        });
    }

    public static void prevStone(long userId) {
        processGame(userId, -1, -1, "prev");
    }

    public static void previewStone(long userId, int x, int y) {
        processGame(userId, x, y, "preview");
    }

    public static void confirmStone(long userId) {
        processGame(userId, -1, -1, "confirm");
    }

    public static void cancelStone(long userId) {
        processGame(userId, -1, -1, "cancel");
    }

    @Deprecated
    public static void placeStone(long userId, int x, int y) {
        processGame(userId, x, y, "place");
    }

    public static void endGame(long messageId) {
        System.out.println("endGame: " + messageId);
        omokMap.remove(messageId);
    }

    public static void resign(long userId) {
        processGame(userId, -1, -1, "resign");
    }

    public static void pullBoard(long userId, MessageChannel channel) {
        Long oldMessageId = getMessageId(userId);
        if (oldMessageId == null)
            throw new NoSuchElementException(DiscordUtil.getNameAsTag(userId) + " 의 오목 대국을 찾을 수 없습니다.");
        OmokGame game = omokMap.get(oldMessageId, "game");
        Long blackId = omokMap.get(oldMessageId, "blackId");
        Long whiteId = omokMap.get(oldMessageId, "whiteId");
        if (!CommonUtil.anySame(blackId, whiteId, userId))
            throw new IllegalCallerException(DiscordUtil.getNameAsTag(userId) + " 님, 당신의 게임이 아닌 것 같은데요...");

        omokMap.replace(oldMessageId, "channel", channel);
        channel.sendMessageEmbeds(getGameEmbed(blackId, whiteId, game).build()).queue(callback -> {
            Long newMessageId = callback.getIdLong();
            omokMap.putAll(newMessageId, omokMap.remove(oldMessageId));
        });
    }

    private static void processGame(long userId, int x, int y, String process) {
        Long messageId = getMessageId(userId);
        System.out.printf("ID: %s process: %s%n", messageId, process);
        if (messageId == null)
            throw new NoSuchElementException(DiscordUtil.getNameAsTag(userId) + " 의 오목 대국을 찾을 수 없습니다.");

        OmokGame game = omokMap.get(messageId, "game");
        Long blackId = omokMap.get(messageId, "blackId");
        Long whiteId = omokMap.get(messageId, "whiteId");
        MessageChannel channel = omokMap.get(messageId, "channel");
        if (CommonUtil.anyNull(game, blackId, whiteId, channel)) {
            logger.warn("omokMap: " + Objects.requireNonNull(omokMap.get(messageId)).values());
            throw new IllegalStateException(DiscordUtil.getNameAsTag(userId) + " 의 오목 게임을 로드하는 중 문제가 발생했습니다.");
        }
        if (!CommonUtil.anySame(blackId, whiteId, userId))
            throw new IllegalCallerException(DiscordUtil.getNameAsTag(userId) + " 님, 당신의 게임이 아닌 것 같은데요...");

        PixelColor stone = getStone(userId, blackId, whiteId);

        switch (process) {
            case "confirm" -> game.confirmStone(stone);
            case "cancel" -> game.unPreview();
            case "place" -> game.placeStone(x, y, stone);
            case "prev" -> game.prevStone();
            case "preview" -> {
                game.previewStone(x, y);
                channel.sendMessageEmbeds(getPreviewEmbed(x, y).build())
                        .setActionRow(
                                Button.success("omok_confirm", "놓기"),
                                Button.danger("omok_cancel", "무르기"))
                        .queue();
            }
            case "resign" -> game.resign(stone);
        }

        if (game.isEnd()) endGame(messageId);
        channel.editMessageEmbedsById(messageId, getGameEmbed(blackId, whiteId, game).build()).queue();
    }

    @NotNull
    private static EmbedBuilder getPreviewEmbed(int x, int y) {
        return new EmbedBuilder()
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .setColor(DiaColor.INFO)
                .addField("미리보기", "여기가 맞나요? x: %s, y: %s".formatted(x, y), false);
    }

    @NotNull
    private static EmbedBuilder getGameEmbed(long blackId, long whiteId, @NotNull OmokGame game) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(OmokGame.BOARD.getColor())
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .addField("오목 게임", game.printBoard(), false)
                .addField(OmokGame.BLACK.getEmoji() + " 흑돌", DiscordUtil.getNameAsTag(blackId), true)
                .addField(OmokGame.WHITE.getEmoji() + " 백돌", DiscordUtil.getNameAsTag(whiteId), true)
                .addField("차례", game.isBlackTurn() ? OmokGame.BLACK.getEmoji() : OmokGame.WHITE.getEmoji(), true)
                .setFooter("경기 중에 채팅은 스크롤이 올라가니 적당히.");
        if (game.isEnd()) embedBuilder.addField(game.getStatus().getDisplay(), "게임 종료", false);
        return embedBuilder;
    }

    @Nullable
    private static Long getMessageId(long userId) {
        AtomicReference<Long> messageId = new AtomicReference<>();
        omokMap.forEach((mainKey, subMap) -> subMap.forEach((subKey, data) -> {
            System.out.printf("main: %s sub: %s value: %s%n", mainKey, subKey, data);
            if ("blackId".equals(subKey) || "whiteId".equals(subKey) && String.valueOf(userId).equals(data)) {
                System.out.println("found: " + data);
                messageId.set(mainKey);
            }
        }));
        return messageId.get();
    }

    private static PixelColor getStone(long userId, Long blackId, Long whiteId) {
        PixelColor stone = PixelColor.ORANGE;
        if (blackId == userId) stone = OmokGame.BLACK;
        if (whiteId == userId) stone = OmokGame.WHITE;
        return stone;
    }

}
