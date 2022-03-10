package net.qsef1256.dacobot.system.account.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonHibernateImpl;
import net.qsef1256.dacobot.enums.DiaColor;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import net.qsef1256.dacobot.system.account.model.Account;
import net.qsef1256.dacobot.util.LocalDateTimeUtil;
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

        try {
            DaoCommon<AccountEntity, Long> dao = new DaoCommonHibernateImpl<>(AccountEntity.class);

            Account user = new Account(eventUser.getIdLong());
            AccountEntity userData = user.getData();

            LocalDateTime lastAttendTime = userData.getLastAttendTime();
            if (lastAttendTime != null && LocalDateTimeUtil.isToday(lastAttendTime)) {
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("이미 출석했습니다.")
                        .setAuthor(eventUser.getName(), null, eventUser.getEffectiveAvatarUrl())
                        .setColor(DiaColor.FAIL)
                        .setDescription("출석 시간: " + LocalDateTimeUtil.getTimeString(lastAttendTime))
                        .build()).queue();
            } else {
                userData.setLastAttendTime(LocalDateTime.now());
                userData.setAttendCount(userData.getAttendCount() + 1);
                dao.save(userData);

                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("출석 체크!")
                        .setAuthor(eventUser.getName(), null, eventUser.getEffectiveAvatarUrl())
                        .setColor(DiaColor.SUCCESS)
                        .appendDescription("정상적으로 출석 체크 되었습니다.\n\n")
                        .appendDescription("출석 횟수: " + userData.getAttendCount())
                        .build()).queue();
            }

        } catch (RuntimeException e) {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("오류 발생")
                    .setAuthor(eventUser.getName(), null, eventUser.getEffectiveAvatarUrl())
                    .setColor(DiaColor.FAIL)
                    .setDescription(e.getMessage())
                    .setFooter("계정 신청은 /계정 등록")
                    .build()).queue();

            if (e instanceof NoSuchElementException) return;
            e.printStackTrace();
        }

    }
}
