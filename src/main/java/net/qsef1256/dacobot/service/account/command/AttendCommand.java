package net.qsef1256.dacobot.service.account.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.service.account.data.UserEntity;
import net.qsef1256.dacobot.service.account.model.Account;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class AttendCommand extends SlashCommand {

    public AttendCommand() {
        name = "출석";
        help = "/다야 attend";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        if (event.getMember() == null) return;
        User eventUser = event.getUser();

        try (DaoCommonJpa<UserEntity, Long> dao = new DaoCommonJpaImpl<>(UserEntity.class)) {
            dao.open();

            Account user = new Account(eventUser.getIdLong());
            UserEntity userData = user.getData();

            LocalDateTime lastAttendTime = userData.getLastAttendTime();
            if (lastAttendTime != null && LocalDateTimeUtil.isToday(lastAttendTime)) {
                event.replyEmbeds(DiaEmbed.fail("이미 출석했습니다.",
                        "출석 시간: " + LocalDateTimeUtil.getTimeString(lastAttendTime),
                        eventUser).build()).queue();
            } else {
                userData.setLastAttendTime(LocalDateTime.now());
                userData.setAttendCount(userData.getAttendCount() + 1);
                dao.save(userData);

                event.replyEmbeds(DiaEmbed.success("출석 체크!",
                        "정상적으로 출석 체크 되었습니다.\n\n" + "출석 횟수: " + userData.getAttendCount(),
                        eventUser).build()).queue();
            }
        } catch (RuntimeException e) {
            event.replyEmbeds(DiaEmbed.fail("오류 발생", e.getMessage(), eventUser)
                    .setFooter("계정 신청은 /계정 등록")
                    .build()).queue();

            if (e instanceof NoSuchElementException) return;
            e.printStackTrace();
        }
    }

}
