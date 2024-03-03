package net.qsef1256.dacobot.game.explosion.domain.itemtype.command;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemTypeCommand extends DacoCommand {

    public ItemTypeCommand() {
        name = "아이템";
        help = "아이템 타입 관리 명령어";

        options = List.of(
                new OptionData(OptionType.STRING, "종류", "명령어 종류")
                        .addChoice("추가", "추가")
                        .addChoice("삭제", "삭제")
        );
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

}
