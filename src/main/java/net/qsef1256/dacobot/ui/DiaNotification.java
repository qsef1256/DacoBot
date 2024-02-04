package net.qsef1256.dacobot.ui;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

@UtilityClass
public class DiaNotification {

    @Setter(onMethod_ = {@Autowired})
    private JdaService jdaService;

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

    public void notifyDM(@NotNull MessageCreateBuilder message, @NotNull User user) {
        user.openPrivateChannel().queue(
                channel -> channel.sendMessage(message.build()).queue(),
                failure -> notify(new MessageCreateBuilder()
                        .addContent("DM 권한을 허가해 주세요.")
                        .addContent("\n")
                        .addContent(message.getContent()), user));
    }

}
