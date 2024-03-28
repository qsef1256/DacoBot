package net.qsef1256.dacobot.command.tool;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class CleanChannelCommand extends DacoCommand {

    public CleanChannelCommand() {
        name = "청소";
        help = "채팅을 지웁니다.";

        ownerCommand = true;
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        event.getChannel().sendMessage(CLEAN_MESSAGE).queue();

        event.reply("채팅 청소가 완료 되었습니다.").queue();
    }

    private static final String CLEAN_MESSAGE =
            """
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    ㅤ
                    """;

}
