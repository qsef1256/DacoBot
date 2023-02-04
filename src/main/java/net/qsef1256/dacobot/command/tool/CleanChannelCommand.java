package net.qsef1256.dacobot.command.tool;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

public class CleanChannelCommand extends SlashCommand {

    private static final String CLEAN_MESSAGE = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
            "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

    public CleanChannelCommand() {
        name = "청소";
        help = "채팅을 지웁니다.";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        event.getChannel().sendMessage(CLEAN_MESSAGE).queue();

        event.reply("채팅 청소가 완료 되었습니다.").queue();
    }

}
