package net.qsef1256.dacobot.module.request.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.module.request.model.Request;
import net.qsef1256.dacobot.module.request.model.RequestAPI;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import net.qsef1256.dacobot.util.JDAService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestCommand extends SlashCommand {


    public RequestCommand() {
        name = "요청";
        help = "요청을 수락하거나 거절합니다.";

        children = new SlashCommand[]{
                new CheckCommand(),
                new AcceptCommand(),
                new DenyCommand(),
                new CancelCommand()
        };
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        SlashCommand[] children = getChildren();

        event.reply(DiaMessage.needSubCommand(children, event.getMember())).queue();
    }

    private static class CheckCommand extends SlashCommand {

        @Setter(onMethod_ = {@Autowired})
        private JDAService jdaService;
        @Setter(onMethod_ = {@Autowired})
        private RequestAPI requestAPI;

        public CheckCommand() {
            name = "확인";
            help = "진행 중인 요청을 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                Request request = requestAPI.getRequest(user.getIdLong());

                event.replyEmbeds(new EmbedBuilder()
                        .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                        .setColor(DiaColor.INFO)
                        .setDescription("진행 중인 요청 정보")
                        .addField("신청자", jdaService.getNameAsTag(request.getRequesterId()), true)
                        .addField("상대자", jdaService.getNameAsTag(request.getReceiverId()), true)
                        .addField("채널", request.getChannel().getName(), true)
                        .addField("종류", request.getTitle(), true)
                        .build()).queue();
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }
        }

    }

    private static class AcceptCommand extends SlashCommand {

        @Setter(onMethod_ = {@Autowired})
        private RequestAPI requestAPI;

        public AcceptCommand() {
            name = "수락";
            help = "진행 중인 요청을 수락합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                requestAPI.accept(user.getIdLong());
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }
        }

    }

    private static class DenyCommand extends SlashCommand {

        @Setter(onMethod_ = {@Autowired})
        private RequestAPI requestAPI;

        public DenyCommand() {
            name = "거절";
            help = "진행 중인 요청을 거절합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                requestAPI.deny(user.getIdLong());
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }
        }

    }

    private static class CancelCommand extends SlashCommand {

        @Setter(onMethod_ = {@Autowired})
        private RequestAPI requestAPI;

        public CancelCommand() {
            name = "취소";
            help = "진행 중인 요청을 취소합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                requestAPI.cancel(user.getIdLong());
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }
        }

    }

}
