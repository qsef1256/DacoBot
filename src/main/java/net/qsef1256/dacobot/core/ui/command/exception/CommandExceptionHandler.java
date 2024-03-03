package net.qsef1256.dacobot.core.ui.command.exception;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Getter
@Component
public abstract class CommandExceptionHandler<T extends Exception> {

    private final Class<T> exception;

    protected CommandExceptionHandler(@NotNull Class<T> exception) {
        this.exception = exception;
    }

    public abstract void onException(@NotNull SlashCommandEvent event,
                                     @NotNull T exception);

}
