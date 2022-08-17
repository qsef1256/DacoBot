package net.qsef1256.dacobot.game.paint.model;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import net.qsef1256.dacobot.game.paint.model.painter.Painter;
import net.qsef1256.dacobot.game.paint.model.painter.PainterContainer;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.util.CommonUtil;
import net.qsef1256.dacobot.util.JDAUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.qsef1256.dacobot.DacoBot.logger;

@UtilityClass
public class PaintDrawer {

    private static final Map<Long, Long> messageMap = new HashMap<>();
    private static final Map<Long, ColorEmoji> selectedColor = new HashMap<>();

    @Nullable
    public static Long getDrawerId(long discordId) {
        return messageMap.get(discordId);
    }

    public static void setDrawerId(long discordId, long messageId) {
        if (messageMap.containsKey(discordId))
            messageMap.replace(discordId, messageId);
        else
            messageMap.put(discordId, messageId);
    }

    @Nullable
    public static ColorEmoji getColor(long discordId) {
        return selectedColor.get(discordId);
    }

    public static void setColor(long discordId, ColorEmoji color) {
        if (selectedColor.containsKey(discordId)) {
            selectedColor.replace(discordId, color);
        } else
            selectedColor.put(discordId, color);
    }

    public static void clearColor(long discordId) {
        selectedColor.remove(discordId);
    }

    public static void setDrawer(@NotNull ButtonClickEvent event, int dx, int dy, boolean isPaint) {
        event.deferEdit().queue();
        Message message = event.getMessage();
        DataObject embedData = message.getEmbeds().get(0).toData();
        DataArray fields = embedData.getArray("fields");

        String userTag = fields.getObject(1).getString("value");
        User user = JDAUtil.getUserFromTag(userTag);
        User eventUser = event.getUser();

        Long drawerId = getDrawerId(eventUser.getIdLong());
        if (drawerId == null) {
            event.getChannel().sendMessage("<@%s> 님, 등록된 그림판이 없어요. /그림 그림판 으로 새로 생성하세요.".formatted(eventUser.getId()))
                    .queue(callback -> callback.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        if (user != eventUser || drawerId != message.getIdLong()) {
            event.getChannel().sendMessage("<@%s> 님, 당신의 그림판이 아닌 것 같은데요... 그림판은 하나만 있을 수 있어요.".formatted(eventUser.getId()))
                    .queue(callback -> callback.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }

        Painter painter = PainterContainer.getPainter(user.getIdLong());
        int x = Integer.parseInt(fields.getObject(2).getString("value"));
        int y = Integer.parseInt(fields.getObject(3).getString("value"));

        x += dx;
        y += dy;

        if (!CommonUtil.isBetween(1, x, painter.getWidth())) {
            if (CommonUtil.getDiff(x, painter.getWidth()) > 1) {
                x = CommonUtil.toBetween(1, x, painter.getWidth());
            } else return;
        }
        if (!CommonUtil.isBetween(1, y, painter.getHeight())) {
            if (CommonUtil.getDiff(y, painter.getHeight()) > 1) {
                y = CommonUtil.toBetween(1, y, painter.getHeight());
            } else return;
        }

        try {
            if (isPaint) {
                ColorEmoji color = getColor(user.getIdLong());
                if (color == null) return;

                painter.paintPixel(color, x, y);
            }
            message.editMessageEmbeds(getDrawerEmbed(user, painter, x, y)).queue();
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            event.getChannel().sendMessage("<@%s> 님, 문제가 생겼네요. 그림판이 고장났어요.".formatted(user.getId())).queue();
        }
    }

    @NotNull
    private static MessageEmbed getDrawerEmbed(@NotNull User user, @NotNull Painter painter, int x, int y) {
        return new EmbedBuilder()
                .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                .setColor(DiaColor.MAIN_COLOR)
                .addField(user.getName() + "의 팔레트", painter.printPallet(), false)
                .addField("유저 태그", user.getAsTag(), true)
                .addField("커서 x", String.valueOf(x), true)
                .addField("커서 y", String.valueOf(y), true)
                .setFooter("/갤러리 저장 으로 그림을 저장하세요.")
                .build();
    }

    public static void initDrawer(@NotNull SlashCommandEvent event) {
        User user = event.getUser();
        event.reply("그림판은 비싸니까 많이 띄우지 마세요. 그리고 천천히 그리세요... 디코가 싫대요.").setEphemeral(true).queue();

        String emptyLabel = "\u200B";
        Painter paint = PainterContainer.getPainter(user.getIdLong());

        event.getChannel()
                .sendMessageEmbeds(getDrawerEmbed(user, paint, 1, 1))
                .setActionRows(
                        ActionRow.of(
                                Button.secondary("paint_drawer_ul", emptyLabel).asDisabled(),
                                Button.primary("paint_drawer_up", emptyLabel).withEmoji(Emoji.fromUnicode("⬆")),
                                Button.secondary("paint_drawer_ur", emptyLabel).asDisabled()),
                        ActionRow.of(
                                Button.primary("paint_drawer_left", emptyLabel).withEmoji(Emoji.fromUnicode("⬅")),
                                Button.success("paint_drawer_center", emptyLabel).withEmoji(Emoji.fromUnicode("✅")),
                                Button.primary("paint_drawer_right", emptyLabel).withEmoji(Emoji.fromUnicode("➡"))),
                        ActionRow.of(
                                Button.secondary("paint_drawer_dl", emptyLabel).asDisabled(),
                                Button.primary("paint_drawer_down", emptyLabel).withEmoji(Emoji.fromUnicode("⬇")),
                                Button.secondary("paint_drawer_dr", emptyLabel).asDisabled()))
                .queue(message -> {
                    for (ColorEmoji color : ColorEmoji.values()) {
                        message.addReaction(color.getEmoji()).queue();
                    }

                    setDrawerId(user.getIdLong(), message.getIdLong());
                    setColor(user.getIdLong(), ColorEmoji.WHITE);
                });
    }
}
