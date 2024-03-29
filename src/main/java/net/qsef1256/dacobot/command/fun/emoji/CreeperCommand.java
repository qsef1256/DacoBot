package net.qsef1256.dacobot.command.fun.emoji;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.core.command.DacoCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class CreeperCommand extends DacoCommand {

    public CreeperCommand() {
        name = "크리퍼";
        help = "Aw, man.";
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        String creeperArt = """
                🟩✳️🟩🟩⬜🟩🟩⬛
                🟩🟩🟩🟩🟩🟩🟩⬜
                🟩⬛⬛🟩🟩⬛⬛⬜
                🟩⬛◽🟩🟩◽⬛🟩
                🟩🟩🟩⬛⬛✳️🟩🟩
                ◼️🟩⬛⬛⬛⬛🟩⬜
                ⬜🟩⬛⬛⬛⬛🟩🟩
                🟩🟩⬛🟩🟩⬛🟩🟩
                """;

        event.reply(creeperArt).queue();
    }

}
