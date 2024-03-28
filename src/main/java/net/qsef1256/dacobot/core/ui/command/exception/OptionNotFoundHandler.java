package net.qsef1256.dacobot.core.ui.command.exception;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.core.ui.command.OptionNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class OptionNotFoundHandler extends CommandExceptionHandler<OptionNotFoundException> {

    protected OptionNotFoundHandler() {
        super(OptionNotFoundException.class);
    }

    @Override
    public void onException(@NotNull SlashCommandEvent event,
                            @NotNull OptionNotFoundException exception) {
        event.reply("%s를 입력해주세요.".formatted(exception.getOptionName()))
                .setEphemeral(true)
                .queue();
    }

}
