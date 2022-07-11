package net.qsef1256.dacobot.service.message.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyMessage implements AbstractMessage { // TODO: implements Controllable

    private Interaction event;
    private Message content;
    private boolean isEphemeral;
    private boolean timed;
    private int afterDestroy;
    private TimeUnit afterDestroyUnit;
    private boolean debug;

    private ReplyMessage() {
        this.content = new MessageBuilder().append("설정된 응답이 없습니다.").build();
        this.isEphemeral = false;
        this.timed = false;
        this.afterDestroy = 5;
        this.afterDestroyUnit = TimeUnit.SECONDS;
        this.debug = false;
    }

    private ReplyMessage(Interaction event) {
        this();
        this.event = event;
    }

    @Contract(" -> new")
    private static @NotNull ReplyMessageBuilder builder() {
        return new ReplyMessageBuilder();
    }

    public static ReplyMessageBuilder builder(Interaction event) {
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
