package net.qsef1256.dacobot.util;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class JDAUtil {

    @Nullable
    public OptionMapping getOptionMapping(@NotNull SlashCommandEvent event, String optionName) {
        OptionMapping option = event.getOption(optionName);
        if (option == null) {
            event.reply("%s를 입력해주세요.".formatted(optionName))
                    .setEphemeral(true)
                    .queue();
            return null;
        }

        return option;
    }

}
