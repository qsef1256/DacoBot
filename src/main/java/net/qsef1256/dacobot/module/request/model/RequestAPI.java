package net.qsef1256.dacobot.module.request.model;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.util.JDAUtil;
import net.qsef1256.dialib.util.CommonUtil;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jetbrains.annotations.NotNull;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 유저간 "신청"을 생성하거나 관리합니다.
 * <p>신청이란 오목 대국 등의, "신청자" 와 "상대자" 가 있고 수락/거절이 가능한 것을 의미합니다.
 *
 * @see Request
 */
@UtilityClass
public class RequestAPI {

    // TODO: integrate message service
    // TODO: for multiple user request (3 ~ N Users)
    private static final BidiMap<Long, Request> requestMap = new DualHashBidiMap<>();

    /**
     * 새로운 유저간 신청을 생성합니다.
     *
     * @param requestObject request Information Object
     * @param <T>           Type of requestObject
     */
    public static <T extends Request> void addRequest(@NotNull T requestObject) {
        String title = requestObject.getTitle();
        long requesterId = requestObject.getRequesterId();
        long receiverId = requestObject.getReceiverId();
        MessageChannel channel = requestObject.getChannel();

        if (requesterId == receiverId) throw new IllegalArgumentException("신청자와 상대자가 같습니다.");
        requestMap.forEach((messageId, request) -> {
            if (CommonUtil.anySame(requesterId, receiverId, request.getRequesterId(), request.getReceiverId())) {
                throw new KeyAlreadyExistsException("이미 진행 중인 신청이 있습니다.");
            }
        });

        User requester = JDAUtil.getUserFromId(requesterId);
        User receiver = JDAUtil.getUserFromId(receiverId);

        channel.sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(requester.getName(), null, requester.getEffectiveAvatarUrl())
                .setColor(DiaColor.INFO)
                .setDescription("요청 중입니다...")
                .build()).queue(callback -> {
            long messageId = callback.getIdLong();

            callback.editMessageEmbeds(new EmbedBuilder()
                            .setAuthor(requester.getName(), null, requester.getEffectiveAvatarUrl())
                            .setColor(DiaColor.INFO)
                            .addField(title, ("<@%s> 님, %s 님으로 부터 %s 신청이 들어왔습니다.\n" +
                                    "수락 또는 거절 버튼을 눌러주세요.").formatted(receiver.getIdLong(), requester.getName(), title), false)
                            .addField("신청자", requester.getName(), true)
                            .addField("상대자", receiver.getName(), true)
                            .addField("신청 ID", String.valueOf(messageId), true)
                            .setFooter("또는 /요청 수락, /요청 거절 명령어를 이용할 수 있습니다.")
                            .build())
                    .setActionRow(
                            Button.primary("request_accept", "수락"),
                            Button.danger("request_deny", "거절")
                    ).queue();
            requestMap.put(messageId, requestObject);
        });
    }

    public static void cancel(long userId) {
        Long messageId = getMessageId(userId);
        if (messageId == null)
            throw new NoSuchElementException("%s 의 신청을 찾을 수 없습니다.".formatted(JDAUtil.getNameAsTag(userId)));

        cancel(userId, messageId);
    }

    private static void cancel(long userId, long messageId) {
        Request request = getRequest(messageId, userId);
        User requester = JDAUtil.getUserFromId(request.getRequesterId());
        User receiver = JDAUtil.getUserFromId(request.getReceiverId());

        if (request.getRequesterId() != userId)
            throw new IllegalCallerException("%s 님, 당신의 메시지가 아닌 것 같은데요...".formatted(JDAUtil.getNameAsTag(userId)));
        request.getChannel().deleteMessageById(messageId).queue();
        request.getChannel().sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(requester.getName(), null, requester.getEffectiveAvatarUrl())
                .setColor(DiaColor.INFO)
                .addField(request.getTitle() + " 취소됨", requester.getName() + " <-> " + receiver.getName() + " 요청이 취소되었습니다.", false)
                .build()).queue();

        requestMap.remove(messageId);
    }

    public static void accept(long userId) {
        Long messageId = getMessageId(userId);
        if (messageId == null)
            throw new NoSuchElementException("%s 의 신청을 찾을 수 없습니다.".formatted(JDAUtil.getNameAsTag(userId)));
        accept(messageId, userId);
    }

    public static void accept(long messageId, long userId) {
        Request request = getRequest(messageId, userId);
        User requester = JDAUtil.getUserFromId(request.getRequesterId());
        User receiver = JDAUtil.getUserFromId(request.getReceiverId());

        if (request.getReceiverId() != userId)
            throw new IllegalCallerException("%s 님, 당신의 메시지가 아닌 것 같은데요...".formatted(JDAUtil.getNameAsTag(userId)));
        request.getChannel().deleteMessageById(messageId).queue();
        request.getChannel().sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(requester.getName(), null, requester.getEffectiveAvatarUrl())
                .setColor(DiaColor.SUCCESS)
                .addField(request.getTitle() + " 수락됨",
                        requester.getName() + " <-> " + receiver.getName() + " 요청이 수락되었습니다.", false)
                .build()).queue();

        request.accept();
        requestMap.remove(messageId);
    }

    public static void deny(long userId) {
        Long messageId = getMessageId(userId);
        if (messageId == null)
            throw new NoSuchElementException("%s 의 신청을 찾을 수 없습니다.".formatted(JDAUtil.getNameAsTag(userId)));

        deny(messageId, userId);
    }

    public static void deny(long messageId, long userId) {
        Request request = getRequest(messageId, userId);
        User requester = JDAUtil.getUserFromId(request.getRequesterId());
        User receiver = JDAUtil.getUserFromId(request.getReceiverId());

        if (request.getReceiverId() != userId)
            throw new IllegalCallerException("%s 님, 당신의 메시지가 아닌 것 같은데요...".formatted(JDAUtil.getNameAsTag(userId)));
        request.getChannel().deleteMessageById(messageId).queue();
        request.getChannel().sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(requester.getName(), null, requester.getEffectiveAvatarUrl())
                .setColor(DiaColor.FAIL)
                .addField(request.getTitle() + " 거절됨",
                        requester.getName() + " <-> " + receiver.getName() + " 요청이 거절되었습니다.", false)
                .build()).queue();

        request.deny();
        requestMap.remove(messageId);
    }

    private static Long getMessageId(long userId) {
        AtomicReference<Long> messageId = new AtomicReference<>();
        requestMap.forEach((id, request) -> {
            if (CommonUtil.anySame(userId, request.getRequesterId(), request.getReceiverId())) {
                messageId.set(id);
            }
        });
        if (messageId.get() == null)
            throw new NoSuchElementException("%s 님의 요청을 찾지 못했습니다.".formatted(JDAUtil.getNameAsTag(userId)));

        return messageId.get();
    }

    private static @NotNull Request getRequest(long messageId, long userId) {
        if (!requestMap.containsKey(messageId))
            throw new NoSuchElementException("%s 님의 요청을 찾지 못했습니다.".formatted(JDAUtil.getNameAsTag(userId)));

        Request request = requestMap.get(messageId);
        if (!CommonUtil.anySame(userId, request.getRequesterId(), request.getReceiverId()))
            throw new IllegalCallerException("%s 님, 당신의 메시지가 아닌 것 같은데요...".formatted(JDAUtil.getNameAsTag(userId)));

        return request;
    }

    public static @NotNull Request getRequest(long userId) {
        Long messageId = getMessageId(userId);

        return getRequest(messageId, userId);
    }

}
