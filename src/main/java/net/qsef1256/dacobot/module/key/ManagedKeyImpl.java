package net.qsef1256.dacobot.module.key;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class ManagedKeyImpl implements ManagedKey {

    private final String type;

    protected ManagedKeyImpl(@NotNull String type) {
        this.type = type;
    }

}
