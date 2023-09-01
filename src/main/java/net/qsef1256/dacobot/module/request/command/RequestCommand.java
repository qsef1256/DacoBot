package net.qsef1256.dacobot.module.request.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.module.request.model.Request;
import net.qsef1256.dacobot.module.request.model.RequestAPI;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import net.qsef1256.dacobot.util.JDAUtil;
import org.jetbrains.annotations.NotNull;

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

        public CheckCommand() {
            name = "확인";
            help = "진행 중인 요청을 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                Request request = RequestAPI.getRequest(user.getIdLong());

                event.replyEmbeds(new EmbedBuilder()
                        .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                        .setColor(DiaColor.INFO)
                        .setDescription("진행 중인 요청 정보")
                        .addField("신청자", JDAUtil.getNameAsTag(request.getRequesterId()), true)
                        .addField("상대자", JDAUtil.getNameAsTag(request.getReceiverId()), true)
                        .addField("채널", request.getChannel().getName(), true)
                        .addField("종류", request.getTitle(), true)
                        .build()).queue();
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }
        }

    }

    private static class AcceptCommand extends SlashCommand {

        public AcceptCommand() {
            name = "수락";
            help = "진행 중인 요청을 수락합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                RequestAPI.accept(user.getIdLong());
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }
        }

    }

    private static class DenyCommand extends SlashCommand {

        public DenyCommand() {
            name = "거절";
            help = "진행 중인 요청을 거절합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                RequestAPI.deny(user.getIdLong());
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }
        }

    }

    private static class CancelCommand extends SlashCommand {

        public CancelCommand() {
            name = "취소";
            help = "진행 중인 요청을 취소합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                RequestAPI.cancel(user.getIdLong());
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }
        }

    }

}
