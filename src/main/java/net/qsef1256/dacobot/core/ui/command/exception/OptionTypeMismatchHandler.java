package net.qsef1256.dacobot.core.ui.command.exception;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.core.ui.command.OptionTypeMismatchException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class OptionTypeMismatchHandler extends CommandExceptionHandler<OptionTypeMismatchException> {

    protected OptionTypeMismatchHandler() {
        super(OptionTypeMismatchException.class);
    }

    @Override
    public void onException(@NotNull SlashCommandEvent event,
                            @NotNull OptionTypeMismatchException exception) {
        event.reply("올바른 %s를 입력하세요. 입력된 값: %s, 필요한 값: %s".formatted(
                        exception.getOptionName(),
                        exception.getOptionValue(),
                        exception.getTargetType()))
                .setEphemeral(true)
                .queue();
    }

}
