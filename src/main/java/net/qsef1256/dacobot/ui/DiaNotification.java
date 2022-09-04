package net.qsef1256.dacobot.ui;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class DiaNotification {

    public void everyone(@NotNull MessageBuilder message) {
        DiaSetting.getMainChannel().sendMessage("@everyone **[%s 공지] %s**".formatted(DiaInfo.BOT_NAME, message.build())).queue();
    }

    public void send(@NotNull MessageBuilder message) {
        DiaSetting.getMainChannel().sendMessage(message.build()).queue();
    }

    public void notify(@NotNull MessageBuilder message, @NotNull User user) {
        DiaSetting.getMainChannel().sendMessage("**[다코봇 알림]** " + user.getAsMention() + ", " + message.build()).queue();
    }

    public void notifyDM(MessageBuilder message, @NotNull User user) {
        user.openPrivateChannel().queue(
                channel -> channel.sendMessage(message.build()).queue(),
                failure -> notify(new MessageBuilder().append("DM 권한을 허가해 주세요.").append("\n").append(message), user));
    }

}
