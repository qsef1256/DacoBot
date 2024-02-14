package net.qsef1256.dacobot.command.tool.moss;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.command.DacoCommand;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import net.qsef1256.dacobot.util.JDAUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MossCommand extends DacoCommand {

    public MossCommand() {
        name = "모스";
        help = "모스 부호 판독기";

        children = new SlashCommand[]{
                new WriteCommand(),
                new ReadCommand()
        };
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        event.reply(DiaMessage.needSubCommand(getChildren(), event.getMember())).queue();
    }

    public static class WriteCommand extends DacoCommand {

        public WriteCommand() {
            name = "쓰기";
            help = "모스 부호를 생성합니다.";

            options = List.of(new OptionData(OptionType.STRING, "내용", "쓸 문장"));
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping option = JDAUtil.getOptionMapping(event, "내용");
            if (option == null) return;

            String input = option.getAsString();

            event.replyEmbeds(DiaEmbed.success("모스 부호", null, event.getUser())
                    .addField("입력", input, true)
                    .addField("출력", MossParser.write(input), true)
                    .build()).queue();
        }

    }

    public static class ReadCommand extends DacoCommand {

        public ReadCommand() {
            name = "읽기";
            help = "모스 부호를 해독합니다.";

            options = List.of(new OptionData(OptionType.STRING, "내용", "해독할 문장"));
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            OptionMapping option = JDAUtil.getOptionMapping(event, "내용");
            if (option == null) return;

            String input = option.getAsString();

            event.replyEmbeds(DiaEmbed.success("모스 부호", null, event.getUser())
                    .addField("입력", input, true)
                    .addField("출력", MossParser.read(input), true)
                    .build()).queue();
        }

    }

}
