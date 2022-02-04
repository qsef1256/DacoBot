package net.qsef1256.diabot.system.request.model;

import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 신청을 나타내는 추상 클래스입니다.
 * <p>
 * 사용을 위해서는 신청에 필요한 정보와 신청 수락/거절 시 실행되어야 할 메서드를 정의해야 합니다.
 * </p>
 *
 * @see RequestManager
 */
@Getter
public abstract class Request {

    private final long requesterId;
    private final long receiverId;
    private final MessageChannel channel;

    protected Request(long requesterId, long receiverId, MessageChannel channel) {
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.channel = channel;
    }

    @NotNull
    public abstract String getTitle();

    @Nullable
    public abstract String getDesc();

    protected abstract void accept();

    protected abstract void deny();

}
