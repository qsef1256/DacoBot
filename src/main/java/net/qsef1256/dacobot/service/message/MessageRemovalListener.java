package net.qsef1256.dacobot.service.message;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.MessageBuilder;
import net.qsef1256.dacobot.localization.TimeLocalizer;
import net.qsef1256.dacobot.service.key.ManagedKey;
import net.qsef1256.dacobot.service.key.UserKey;
import net.qsef1256.dacobot.service.message.data.MessageData;
import net.qsef1256.dacobot.ui.DiaNotification;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class MessageRemovalListener implements RemovalListener<ManagedKey, MessageData> {

    @Override
    public void onRemoval(@NotNull RemovalNotification<ManagedKey, MessageData> notification) {
        if (notification.getKey() instanceof UserKey key) {
            key.getUsers().forEach(user -> {
                String removeCause;

                if (!notification.wasEvicted()) return;
                switch (notification.getCause()) {
                    case EXPIRED ->
                            removeCause = "제한 시간 초과 (%s)".formatted(TimeLocalizer.format(MessageAPI.getExpireAfterWrite()));
                    case SIZE -> removeCause = "너무 많은 요청 (%s)".formatted(MessageAPI.getCacheSize());
                    case COLLECTED -> removeCause = "GC 수집으";
                    default -> {
                        log.info("message removed for unknown reason: %s, key: %s"
                                .formatted(notification.getCause(), notification.getKey()));
                        removeCause = "알 수 없는 이유";
                    }
                }

                if (notification.getValue() == null) return;

                notification.getValue().getOnRemove().run();
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.append(key.getType());
                messageBuilder.append(" 메시지가 %s로 삭제 되었습니다.".formatted(removeCause));

                DiaNotification.notify(messageBuilder, user);
            });
        }
    }

}
