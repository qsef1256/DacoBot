package net.qsef1256.dacobot.service.message;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.service.key.ManagedKey;
import net.qsef1256.dacobot.service.message.data.MessageData;
import net.qsef1256.dacobot.service.message.exception.MessageApiException;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.time.Duration;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 특정한 Key 에 따라 관리되는 메시지를 저장하고 가져옵니다.
 * <p>제한 시간이 지정되어 있으며, 지날시 자동 삭제됩니다.</p>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageApiImpl implements MessageApi {

    @Getter
    private static final int CACHE_SIZE = 10000;
    @Getter
    private static final Duration EXPIRE_AFTER_WRITE = Duration.ofDays(1);
    @Getter
    private static final MessageApiImpl instance = new MessageApiImpl();

    private final LoadingCache<ManagedKey, MessageData> keyCache = Caffeine.newBuilder()
            .maximumSize(CACHE_SIZE)
            .expireAfterWrite(EXPIRE_AFTER_WRITE)
            .removalListener(new MessageRemovalListener<>())
            .build(new CacheLoader<>() {
                @Override
                public @Nullable MessageData load(ManagedKey key) {
                    return keyCache.get(key);
                }
            });

    @Override
    public void add(ManagedKey key, MessageData snowflake) {
        if (has(key)) throw new MessageApiException("이미 등록된 snowflake 가 있습니다: " + key);

        keyCache.put(key, snowflake);
    }

    @Override
    public MessageData get(ManagedKey key) {
        if (!has(key)) throw new NoSuchElementException("해당 snowflake 를 찾을 수 없습니다: " + key);

        try {
            return keyCache.get(key);
        } catch (RuntimeException e) {
            throw new MessageApiException(e);
        }
    }

    @Override
    public void remove(ManagedKey key) {
        keyCache.invalidate(key);
    }

    @Override
    public boolean has(ManagedKey key) {
        return keyCache.asMap().containsKey(key);
    }

    @Override
    public void refresh(ManagedKey key) {
        keyCache.refresh(key);
    }

    @Override
    @TestOnly
    public void clear() {
        keyCache.invalidateAll();
    }

    @TestOnly
    public CacheStats getStatus() {
        return keyCache.stats();
    }

    public Map<ManagedKey, MessageData> getAsMap() {
        return keyCache.asMap();
    }

}
