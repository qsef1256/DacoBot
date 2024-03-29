package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.qsef1256.dacobot.core.command.DacoCommand;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class NoZaraCommand extends DacoCommand {

    public NoZaraCommand() {
        name = "용봉탕";
        help = "용봉탕은 자라와 닭 또는 오리 등의 가금류, 전복 등을 주재료로 하여 만든 보양식입니다.";

        statistic = true;
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        EmbedBuilder embedBuilder = DiaEmbed.primary("용봉탕", "저런! 자라가 도망갔네요.", null);
        embedBuilder.setImage("https://media.tenor.com/Yu00bIKQgtAAAAAd/mcconnell-turtle.gif");
        embedBuilder.setFooter(getUseInfo());

        event.replyEmbeds(embedBuilder.build()).queue();
    }

}
