package net.qsef1256.dacobot.game.paint.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.qsef1256.dacobot.game.paint.command.PaintCommand;
import net.qsef1256.dacobot.game.paint.model.PaintDrawer;
import net.qsef1256.dacobot.game.paint.model.PaintManagerImpl;
import net.qsef1256.dacobot.game.paint.model.painter.PainterContainer;
import net.qsef1256.dacobot.service.cmdstat.CmdStatistic;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

import static net.qsef1256.dacobot.DacoBot.logger;

public class PaintButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(final @NotNull ButtonInteractionEvent event) {
        switch (event.getComponentId()) {

            case "paint_erase" -> {
                if (event.getMember() == null) return;
                User user = event.getUser();

                try {
                    CmdStatistic statistic = new CmdStatistic(PaintCommand.EraseCommand.class);

                    PainterContainer.getPainter(user.getIdLong()).erasePallet();
                    event.replyEmbeds(new EmbedBuilder()
                            .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                            .setColor(DiaColor.SUCCESS)
                            .setTitle("쓱쓱 싹싹")
                            .setDescription("다이아의 용량이 절약 되었습니다.")
                            .setFooter("용량 절약 횟수: " + statistic.getUseCount() + " 금일: " + statistic.getTodayUsed())
                            .build()).queue();
                } catch (RuntimeException e) {
                    logger.warn(e.getMessage());
                    event.replyEmbeds(new EmbedBuilder()
                            .setColor(DiaColor.FAIL)
                            .setTitle("문제 발생")
                            .setDescription("그림판을 지우던 도중 문제가 발생했습니다.")
                            .setFooter("다이아의 용량이 줄줄 새고 있습니다!")
                            .build()).queue();
                }

                event.editButton(event.getButton().asDisabled()).queue();
            }

            case "gallery_overwrite" -> {
                if (event.getMember() == null) return;
                User user = event.getUser();

                event.deferReply().queue(callback -> {
                    try {
                        String paintName = event.getMessage().getEmbeds().get(0).getFields().get(0).getValue();
                        if (paintName == null)
                            throw new NoSuchElementException("그림 이름을 받아오는데 실패했습니다.");

                        new PaintManagerImpl().overwrite(user.getIdLong(), paintName);
                        callback.editOriginalEmbeds(new EmbedBuilder()
                                .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                                .setColor(DiaColor.SUCCESS)
                                .setTitle("그림 덮어쓰기 성공")
                                .setDescription(paintName + " 그림을 덮어썼습니다.")
                                .build()).queue();
                    } catch (RuntimeException e) {
                        logger.warn(e.getMessage());
                        callback.editOriginalEmbeds(new EmbedBuilder()
                                .setColor(DiaColor.FAIL)
                                .setTitle("문제 발생")
                                .setDescription("그림을 덮어 쓰던 중 문제가 발생했습니다.")
                                .setFooter("아무래도 그림이 삭제되길 거부하는 모양이네요...")
                                .build()).queue();
                    }
                });

                event.editButton(event.getButton().asDisabled()).queue();
            }

            case "paint_drawer_up" -> PaintDrawer.setDrawer(event, 0, -1, false);
            case "paint_drawer_down" -> PaintDrawer.setDrawer(event, 0, 1, false);
            case "paint_drawer_left" -> PaintDrawer.setDrawer(event, -1, 0, false);
            case "paint_drawer_right" -> PaintDrawer.setDrawer(event, 1, 0, false);
            case "paint_drawer_center" -> PaintDrawer.setDrawer(event, 0, 0, true);
            default -> {
            }
        }
    }

}
