package net.qsef1256.dacobot.service.message.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.service.key.SingleUserKey;
import net.qsef1256.dacobot.service.message.type.TrackedEventMessage;
import net.qsef1256.dacobot.ui.DiaMessage;
import net.qsef1256.dacobot.util.JDAUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageCommand extends SlashCommand {

    public MessageCommand() {
        name = "메시지";
        help = "메시지 서비스 테스트 명령어";
        ownerCommand = true;

        children = new SlashCommand[]{
                new SendCommand(),
                new EditCommand(),
                new RemoveCommand(),
                new MoveCommand()
        };
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        DiaMessage.needSubCommand(this.children, event.getMember());
    }

    public static class SendCommand extends SlashCommand {

        public SendCommand() {
            name = "보내기";
            help = "메시지를 보냅니다";

            options = List.of(
                    new OptionData(OptionType.STRING, "메시지", "보낼 내용", true)
            );
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            OptionMapping option = JDAUtil.getOptionMapping(event, "메시지");
            if (option == null) return;

            SingleUserKey key = new SingleUserKey("test", event.getUser());
            MessageBuilder message = new MessageBuilder().append(option.getAsString());

            new TrackedEventMessage(key, message, event).send();
        }

    }

    public static class EditCommand extends SlashCommand {

        public EditCommand() {
            name = "수정";
            help = "메시지를 수정합니다.";

            options = List.of(
                    new OptionData(OptionType.STRING, "메시지", "수정할 내용", true)
            );
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            OptionMapping messageOptions = event.getOption("메시지");

            if (messageOptions == null) {
                event.reply("수정할 메시지를 입력하세요.").queue();
                return;
            }

            SingleUserKey key = new SingleUserKey("test", event.getUser());
            MessageBuilder message = new MessageBuilder().append(messageOptions.getAsString());

            new TrackedEventMessage(key, message, event).edit(message);
        }

    }

    public static class RemoveCommand extends SlashCommand {

        public RemoveCommand() {
            name = "삭제";
            help = "메시지를 지웁니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            SingleUserKey key = new SingleUserKey("test", event.getUser());

            new TrackedEventMessage(key, new MessageBuilder(), event).remove();
        }

    }
    
    public static class MoveCommand extends SlashCommand {

        public MoveCommand() {
            name = "이동";
            help = "메시지를 이동합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            SingleUserKey key = new SingleUserKey("test", event.getUser());

            new TrackedEventMessage(key, new MessageBuilder(), event).move(event.getChannel());
        }

    }

}
