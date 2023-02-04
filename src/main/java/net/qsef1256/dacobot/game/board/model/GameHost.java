package net.qsef1256.dacobot.game.board.model;

import lombok.Getter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.qsef1256.dacobot.service.key.ManagedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 게임을 컨트롤 합니다.
 *
 * @param <T> Game type
 */
public abstract class GameHost<T extends Game> implements Game {

    private T game;

    @Getter
    private final ManagedKey key;

    protected GameHost(ManagedKey key) {
        this.key = key;
    }

    public T getGame() {
        if (game == null) throw new IllegalStateException("게임이 시작되지 않았습니다: " + key.getType());

        return game;
    }

    public void setGame(T game) {
        this.game = game;
    }

    @Override
    public void resign(long userId) {
        game.resign(userId);
    }

    @Override
    public void win(long userId) {
        game.win(userId);
    }

    @Override
    public boolean playerExist(long userId) {
        return game.playerExist(userId);
    }

    public @NotNull MessageCreateBuilder getUIMessage() {
        return game.getUI().getUIMessage();
    }

    public @NotNull String getUIString() {
        return game.getUI().getUIString();
    }

    public abstract void init();

    @Override
    public String toString() {
        return "%s@%s[key = %s, game = %s]".formatted(getClass().getSimpleName(), hashCode(), key, game);
    }

}
