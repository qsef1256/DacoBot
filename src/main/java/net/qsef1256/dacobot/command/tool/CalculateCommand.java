package net.qsef1256.dacobot.command.tool;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.service.notification.DiaEmbed;
import net.qsef1256.dacobot.setting.enums.DiaColor;
import org.jetbrains.annotations.NotNull;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.List;

import static net.qsef1256.dacobot.DacoBot.logger;

public class CalculateCommand extends SlashCommand {

    public CalculateCommand() {
        name = "계산";
        help = "mXParser를 이용한 계산을 합니다.";

        options = List.of(new OptionData(OptionType.STRING, "수식", "계산 할 수식").setRequired(true));
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        if (event.getMember() == null) return;

        final OptionMapping option = event.getOption("수식");
        if (option == null) {
            event.reply("메시지를 입력해주세요.").setEphemeral(true).queue();
            return;
        }

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
            logger.warn(e.getMessage());
            event.replyEmbeds(DiaEmbed.error(null, "주어진 문자열을 계산하던 도중 문제가 생겼습니다.", null, null)
                    .addField("입력한 값", formula, false)
                    .setFooter("뭘 넣었길래...")
                    .build()).queue();
        }
    }

}
