package net.qsef1256.dacobot.module.message;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.qsef1256.dacobot.core.localization.TimeLocalizer;
import net.qsef1256.dacobot.module.common.key.ManagedKey;
import net.qsef1256.dacobot.module.common.key.UserKey;
import net.qsef1256.dacobot.module.message.data.MessageData;
import net.qsef1256.dacobot.ui.DiaNotification;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageRemovalListener<K1 extends ManagedKey, V1 extends MessageData> implements RemovalListener<K1, V1> {

    @Setter(onMethod_ = {@Autowired})
    public JDA jda;

    @Override
    public void onRemoval(@Nullable K1 key,
                          @Nullable V1 value,
                          RemovalCause cause) {
        if (jda == null) return;

        if (key instanceof UserKey userKey) {
            userKey.getUsers().forEach(user -> {
                String removeCause;

                if (!cause.wasEvicted()) return;
                switch (cause) {
                    case EXPIRED ->
                            removeCause = "제한 시간 초과 (%s)".formatted(TimeLocalizer.format(MessageApiImpl.getEXPIRE_AFTER_WRITE()));
                    case SIZE -> removeCause = "너무 많은 요청 (%s)".formatted(MessageApiImpl.getCACHE_SIZE());
                    case COLLECTED -> removeCause = "GC 수집으";
                    default -> {
                        log.info("message removed for unknown reason: %s, key: %s"
                                .formatted(cause, key));
                        removeCause = "알 수 없는 이유";
                    }
                }

                if (value == null) return;

                value.getOnRemove().run();
                MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
                messageBuilder.addContent(userKey.getType());
                messageBuilder.addContent(" 메시지가 %s로 삭제 되었습니다.".formatted(removeCause));

                DiaNotification.notify(messageBuilder, user);
            });
        }
    }

}
