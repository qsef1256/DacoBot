package net.qsef1256.diabot.command.fun.emoji;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

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
