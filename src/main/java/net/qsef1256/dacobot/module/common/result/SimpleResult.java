package net.qsef1256.dacobot.module.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class SimpleResult implements AbstractResult {

    private boolean success;
    private String message;

    @Contract("_ -> new")
    public static @NotNull SimpleResult success(String message) {
        return new SimpleResult(true, message);
    }

    @Contract("_ -> new")
    public static @NotNull SimpleResult failure(String message) {
        return new SimpleResult(false, message);
    }

}
