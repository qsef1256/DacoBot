package net.qsef1256.dacobot.module.punish.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.module.punish.controller.PunishController;
import net.qsef1256.dacobot.module.punish.entity.PunishType;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.util.TimeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class PunishCommand extends SlashCommand {

    public PunishCommand() {
        name = "처벌";
        help = "처벌 명령어, 관리진 전용";

        ownerCommand = true;
        OptionData type = new OptionData(OptionType.STRING, "종류", "처벌 종류", true);
        for (PunishType punishType : PunishType.values())
            type.addChoice(punishType.getName(), punishType.getDesc());

        options = List.of(type,
                new OptionData(OptionType.STRING, "사유", "처벌 사유"),
                new OptionData(OptionType.USER, "대상자", "처벌 대상자"),
                new OptionData(OptionType.STRING, "기간", "처벌 기간, hms, 예시: 10m 30s"));
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        String typeInput = Objects.requireNonNull(event.getOption("종류")).getAsString();

        PunishType punishType = PunishType.fromName(typeInput);
        if (punishType == null) {
            event.replyEmbeds(DiaEmbed.error(
                    "알 수 없는 처벌",
                    "알 수 없는 처벌입니다: " + typeInput, null, null).build()).queue();
            return;
        }

        OptionMapping reasonOption = event.getOption("사유");
        String reason = reasonOption != null
                ? reasonOption.getAsString()
                : "사유 없음";

        User user = Objects.requireNonNull(event.getOption("대상자")).getAsUser();
        OptionMapping durationOption = event.getOption("기간");
        Duration duration = durationOption != null
                ? TimeUtil.parseHms(durationOption.getAsString())
                : Duration.ZERO;

        PunishController.getInstance().punish(user, punishType, reason, duration);
    }

}
