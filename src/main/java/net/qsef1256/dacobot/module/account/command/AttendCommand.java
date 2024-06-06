package net.qsef1256.dacobot.module.account.command;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.module.account.user.UserController;
import net.qsef1256.dacobot.module.account.user.UserEntity;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class AttendCommand extends DacoCommand {

    private final UserController userController;

    public AttendCommand(@NotNull UserController userController) {
        this.userController = userController;

        name = "출석";
        help = "/다야 attend";
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        if (event.getMember() == null) return;
        User eventUser = event.getUser();

        try {
            UserEntity userData = userController.getUser(eventUser.getIdLong());

            LocalDateTime lastAttendTime = userData.getLastAttendTime();
            if (lastAttendTime != null && LocalDateTimeUtil.isToday(lastAttendTime)) {
                event.replyEmbeds(DiaEmbed.fail("이미 출석했습니다.",
                        "출석 시간: " + LocalDateTimeUtil.getTimeString(lastAttendTime),
                        eventUser).build()).queue();
            } else {
                userData.setLastAttendTime(LocalDateTime.now());
                userData.setAttendCount(userData.getAttendCount() + 1);
                userController.saveUser(userData);

                event.replyEmbeds(DiaEmbed.success("출석 체크!",
                        "정상적으로 출석 체크 되었습니다.\n\n" + "출석 횟수: " + userData.getAttendCount(),
                        eventUser).build()).queue();
            }
        } catch (RuntimeException e) {
            event.replyEmbeds(DiaEmbed.fail("오류 발생", e.getMessage(), eventUser)
                    .setFooter("계정 신청은 /계정 등록")
                    .build()).queue();

            if (e instanceof NoSuchElementException) return;
            log.error("failed to attend of %s".formatted(event.getUser().getName()), e);
        }
    }

}
