package net.qsef1256.dacobot.command.tool.hangeul;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.util.JDAUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.qsef1256.dacobot.DacoBot.logger;

@Component
public class EngKorConvertCommand extends SlashCommand {

    public EngKorConvertCommand() {
        name = "영한";
        help = "영한 오타를 변환합니다.";

        options = List.of(new OptionData(OptionType.STRING, "오타", "변환 할 오타").setRequired(true));
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        try {
            OptionMapping option = JDAUtil.getOptionMapping(event, "오타");
            if (option == null) return;

            event.replyEmbeds(DiaEmbed.info("영한 변환",
                    KorEngConverter.getInstance().engToKor(option.getAsString()),
                    event.getUser()).build()).queue();
        } catch (RuntimeException e) {
            logger.warn(e.getMessage());
            event.replyEmbeds(DiaEmbed.error("오타", "주어진 문자열을 변환하던 도중 문제가 생겼습니다.", null, null)
                    .setFooter("뭘 넣었길래...")
                    .build()).queue();
        }
    }

}
