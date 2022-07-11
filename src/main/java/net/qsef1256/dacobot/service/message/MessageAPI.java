package net.qsef1256.dacobot.service.message;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.service.key.ManagedKey;
import net.qsef1256.dacobot.service.message.data.MessageData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

/**
 * 특정한 Key 에 따라 관리되는 메시지를 저장하고 가져옵니다.
 * <p>제한 시간이 지정되어 있으며, 지날시 자동 삭제됩니다.</p>
 */
@UtilityClass
@Slf4j
public class MessageAPI {

    @Getter
    private final int cacheSize = 10000;
    @Getter
    private final Duration expireAfterWrite = Duration.ofDays(5);

    private final LoadingCache<ManagedKey, MessageData> keyCache = CacheBuilder.newBuilder()
            .maximumSize(cacheSize)
            .expireAfterWrite(expireAfterWrite)
            .removalListener(new MessageRemovalListener())
            .build(new CacheLoader<>() {
                @Override
                @NotNull
                public MessageData load(@NotNull ManagedKey key) throws ExecutionException {
                    return keyCache.get(key);
                }
            });

    public void add(ManagedKey key, MessageData snowflake) {
        if (has(key)) throw new DuplicateRequestException("이미 등록된 snowflake 가 있습니다: " + key);

        keyCache.put(key, snowflake);
    }

    public MessageData get(ManagedKey key) {
        if (!has(key)) throw new NoSuchElementException("해당 snowflake 를 찾을 수 없습니다: " + key);

        try {
            return keyCache.get(key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(ManagedKey key) {
        keyCache.invalidate(key);
    }

    public boolean has(ManagedKey key) {
        return keyCache.getIfPresent(key) != null;
    }

    public void refresh(ManagedKey key) {
        keyCache.refresh(key);
    }

    @TestOnly
    public void clear() {
        keyCache.invalidateAll();
    }

}
