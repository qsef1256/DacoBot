package net.qsef1256.dacobot.module.diapedia.command;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.command.DacoCommand;
import net.qsef1256.dacobot.module.diapedia.Diapedia;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiapediaCommand extends DacoCommand {

    public DiapediaCommand() {
        name = "사전";
        help = "다이아 백과사전";

        options = List.of(
                new OptionData(OptionType.STRING, "검색어", "검색할 내용", false)
        );
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        String search = event.optString("검색어");

        if (search == null) {
            event.replyEmbeds(Diapedia
                    .getInstance()
                    .getIndex()).queue();
        } else {
            event.replyEmbeds(Diapedia
                    .getInstance()
                    .search(search)).queue();
        }
    }

}
