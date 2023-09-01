package net.qsef1256.dacobot.module.punish.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PunishCommand extends SlashCommand {

    public PunishCommand() {
        name = "처벌";
        help = "처벌 명령어, 관리진 전용";

        ownerCommand = true;
        options = List.of(
                new OptionData(OptionType.STRING, "종류", "처벌 종류", true)
                        .addChoice("경고", "경고 처벌, 처벌 목록에 사유가 기록됩니다.")
                        .addChoice("밴", "밴 처벌, 모든 명령어를 사용할 수 없게 합니다."),
                new OptionData(OptionType.STRING, "사유", "처벌 사유"));
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        String type = event.getOption("종류").getAsString();
    }

}
