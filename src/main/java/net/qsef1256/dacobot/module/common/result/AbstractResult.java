package net.qsef1256.dacobot.module.common.result;

import org.jetbrains.annotations.Nullable;

public interface AbstractResult {

    boolean isSuccess();

    default boolean isFailure() {
        return !isSuccess();
    }

    @Nullable
    String getMessage();

}
