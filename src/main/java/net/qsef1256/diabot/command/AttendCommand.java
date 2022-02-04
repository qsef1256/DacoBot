package net.qsef1256.diabot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.diabot.data.DiscordUserEntity;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.model.DiscordUser;
import net.qsef1256.diabot.util.LocalDateUtil;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class AttendCommand extends SlashCommand {

    public AttendCommand() {
        name = "출석";
        help = "/다야 attend";
    }

    @Override
    protected void execute(SlashCommandEvent event) {

        if (event.getMember() == null) return;
        User eventUser = event.getUser();

        try {
            DaoCommon<Long, DiscordUserEntity> dao = new DaoCommonImpl<>(DiscordUserEntity.class);

            DiscordUser user = new DiscordUser(eventUser.getIdLong());
            DiscordUserEntity userData = user.getData();

            LocalDateTime lastAttendTime = userData.getLastAttendTime();
            if (lastAttendTime != null && LocalDateUtil.isToday(lastAttendTime)) {
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("이미 출석했습니다.")
                        .setAuthor(eventUser.getName(), null, eventUser.getEffectiveAvatarUrl())
                        .setColor(DiaColor.FAIL)
                        .setDescription("출석 시간: " + LocalDateUtil.getTimeString(lastAttendTime))
                        .build()).queue();
            } else {
                userData.setLastAttendTime(LocalDateTime.now());
                userData.setAttendCount(userData.getAttendCount() + 1);
                dao.update(userData);

                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("출석 체크!")
                        .setAuthor(eventUser.getName(), null, eventUser.getEffectiveAvatarUrl())
                        .setColor(DiaColor.SUCCESS)
                        .setDescription("정상적으로 출석 체크 되었습니다.")
                        .setDescription("출석 횟수: " + userData.getAttendCount())
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
