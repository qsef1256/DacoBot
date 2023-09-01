package net.qsef1256.dacobot.module.message.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyMessage implements AbstractMessage { // TODO: implements Controllable

    private IReplyCallback event;
    private MessageCreateData content;
    private boolean isEphemeral;
    private boolean timed;
    private int afterDestroy;
    private TimeUnit afterDestroyUnit;
    private boolean debug;

    private ReplyMessage() {
        this.content = new MessageCreateBuilder().addContent("설정된 응답이 없습니다.").build();
        this.isEphemeral = false;
        this.timed = false;
        this.afterDestroy = 5;
        this.afterDestroyUnit = TimeUnit.SECONDS;
        this.debug = false;
    }

    private ReplyMessage(IReplyCallback event) {
        this();
        this.event = event;
    }

    @Contract(" -> new")
    private static @NotNull ReplyMessageBuilder builder() {
        return new ReplyMessageBuilder();
    }

    public static ReplyMessageBuilder builder(IReplyCallback event) {
        return builder().event(event);
    }

    @Override
    public void send() {
        try {
            event.reply(content).setEphemeral(isEphemeral).queue(result -> {
                if (timed) result.deleteOriginal().queueAfter(afterDestroy, afterDestroyUnit);
            });
        } catch (RuntimeException e) {
            if (debug) log.error("Reply failed: " + e);

            throw e;
        }
    }

}
