package net.qsef1256.dacobot.command.fun.emoji;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class CreeperCommand extends SlashCommand {

    public CreeperCommand() {
        name = "크리퍼";
        help = "Aw, man.";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
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
