package net.qsef1256.dacobot.ui;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class DiaNotification {

    private final JdaService jdaService;

    public DiaNotification(@NotNull JdaService jdaService) {
        this.jdaService = jdaService;
    }

    public void everyone(@NotNull MessageCreateBuilder message) {
        jdaService.getMainChannel().sendMessage(
                "@everyone **[%s 공지] %s**".formatted(DiaInfo.BOT_NAME, message.build())).queue();
    }

    public void send(@NotNull MessageCreateBuilder message) {
        jdaService.getMainChannel().sendMessage(message.build()).queue();
    }

    public void notify(@NotNull MessageCreateBuilder message, @NotNull User user) {
        jdaService.getMainChannel().sendMessage(
                "**[다코봇 알림]** " + user.getAsMention() + ", " + message.build()).queue();
    }

    public void notifyDirectMessage(@NotNull MessageCreateBuilder message, @NotNull User user) {
        user.openPrivateChannel().queue(
                channel -> channel.sendMessage(message.build()).queue(),
                failure -> notify(new MessageCreateBuilder()
                        .addContent("다이렉트 메시지(DM) 권한을 허가해 주세요.")
                        .addContent("\n")
                        .addContent(message.getContent()), user));
    }

}
