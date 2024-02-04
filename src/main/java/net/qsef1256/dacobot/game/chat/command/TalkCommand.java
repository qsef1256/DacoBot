package net.qsef1256.dacobot.game.chat.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.qsef1256.dacobot.ui.DiaMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

// TODO: add talk
@Component
public class TalkCommand extends Command {

    public TalkCommand() {
        name = "대화";
        help = "대화 추가/삭제 등 기능 명령어";

        children = new Command[]{
                new ReloadCommand()
        };
    }

    @Override
    protected void execute(@NotNull CommandEvent event) {
        event.reply(DiaMessage.needSubCommand(children, event.getMember()));
    }

    private static class ReloadCommand extends Command {

        public ReloadCommand() {
            name = "리로드";
            help = "채팅 시스템을 리로드 합니다.";

            ownerCommand = true;
        }

        @Override
        protected void execute(@NotNull CommandEvent event) {
            event.reply(DiaMessage.underConstruction());
        }

    }

}
