package net.qsef1256.dacobot.command.tool;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.command.DacoCommand;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.util.JDAUtil;
import org.jetbrains.annotations.NotNull;
import org.mariuszgromada.math.mxparser.Expression;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CalculateCommand extends DacoCommand {

    public CalculateCommand() {
        name = "계산";
        help = "mXParser를 이용한 계산을 합니다.";

        options = List.of(new OptionData(OptionType.STRING, "수식", "계산 할 수식").setRequired(true));
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        if (event.getMember() == null) return;

        OptionMapping option = JDAUtil.getOptionMapping(event, "수식");
        if (option == null) return;

        User user = event.getUser();
        String formula = option.getAsString();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());

        try {
            Expression expression = new Expression(formula);
            String result;

            if (expression.checkSyntax()) {
                result = String.valueOf(expression.calculate());
                embedBuilder.setColor(DiaColor.INFO);
            } else {
                result = "잘못된 수식 입니다.";
                embedBuilder.setColor(DiaColor.FAIL);
            }

            event.replyEmbeds(embedBuilder
                    .addField("계산 결과", "입력한 값: `" + formula + "`\n계산된 값: `" + result + "`", false)
                    .setFooter("provided by mXParser")
                    .build()).queue();
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            event.replyEmbeds(DiaEmbed.error(null, "주어진 문자열을 계산하던 도중 문제가 생겼습니다.", null, null)
                    .addField("입력한 값", formula, false)
                    .setFooter("뭘 넣었길래...")
                    .build()).queue();
        }
    }

}
