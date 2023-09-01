package net.qsef1256.dacobot.game.board;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.qsef1256.dacobot.game.board.model.GameHost;
import net.qsef1256.dacobot.module.key.ManagedKey;
import net.qsef1256.dacobot.module.message.MessageApiImpl;
import net.qsef1256.dacobot.module.message.type.TrackedEventMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@UtilityClass
public class GameAPI {

    private static final Map<ManagedKey, GameHost<?>> gameMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getGame(@NotNull GameHost<?> game) {
        if (!gameMap.containsKey(game.getKey()))
            throw new NoSuchElementException(game.getKey().getType() + " 게임을 찾지 못했습니다. 아직 시작하지 않았다면 게임을 시작하세요!");

        return (T) gameMap.get(game.getKey());
    }

    public void newGame(@NotNull GameHost<?> game, IReplyCallback event) {
        if (gameMap.containsKey(game.getKey()))
            throw new DuplicateRequestException(game.getKey().getType() + " 게임이 이미 있습니다.");
        if (!event.getChannelType().isMessage()) throw new IllegalArgumentException("메시지 채널에만 보낼 수 있습니다.");

        game.init();

        new TrackedEventMessage(game.getKey(), game.getUIMessage(), event, () -> removeGame(game.getKey())).send();
        gameMap.put(game.getKey(), game);
    }

    public void removeGame(ManagedKey message) {
        if (!MessageApiImpl.getInstance().has(message))
            throw new NoSuchElementException(message.getType() + " 게임을 찾지 못했습니다.");

        gameMap.remove(message);
    }

    @TestOnly
    public void clear() {
        gameMap.clear();
    }

}
