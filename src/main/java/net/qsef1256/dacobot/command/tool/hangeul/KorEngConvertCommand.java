package net.qsef1256.dacobot.command.tool.hangeul;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.ui.DiaMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KorEngConvertCommand extends SlashCommand {

    public KorEngConvertCommand() {
        name = "한영";
        help = "한영 오타를 변환합니다.";

        options = List.of(new OptionData(OptionType.STRING, "오타", "변환 할 오타").setRequired(true));
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        event.reply(DiaMessage.underConstruction()).queue(); // TODO
    }

}