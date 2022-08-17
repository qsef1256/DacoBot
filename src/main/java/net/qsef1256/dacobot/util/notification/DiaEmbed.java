package net.qsef1256.dacobot.util.notification;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class DiaEmbed {

    /**
     * 오류 임베드를 작성합니다. 모든 항목은 없을 수 있습니다.
     *
     * @param title Error title
     * @param desc  description
     * @param e     Exception
     * @param user  target user
     * @return error message EmbedBuilder
     */
    @NotNull
    public static EmbedBuilder error(@Nullable String title, @Nullable String desc, Throwable e, @Nullable User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(DiaColor.FAIL);
        embedBuilder.setTitle(title != null ? title : "오류 발생");
        setDesc(desc, embedBuilder);
        setUser(user, embedBuilder);
        if (e != null) embedBuilder.addField("오류 정보", e.getMessage(), false);
        embedBuilder.setFooter("문제가 계속될 시 관리자에게 연락 해주세요.");

        return embedBuilder;
    }

    @NotNull
    public static EmbedBuilder main(@Nullable String title, @Nullable String desc, @Nullable User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(DiaColor.MAIN_COLOR);
        setTitle(title, embedBuilder);
        setDesc(desc, embedBuilder);
        setUser(user, embedBuilder);

        return embedBuilder;
    }

    @NotNull
    public static EmbedBuilder info(@Nullable String title, @Nullable String desc, @Nullable User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(DiaColor.INFO);
        setTitle(title, embedBuilder);
        setDesc(desc, embedBuilder);
        setUser(user, embedBuilder);

        return embedBuilder;
    }

    @NotNull
    public static EmbedBuilder success(@Nullable String title, @Nullable String desc, @Nullable User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(DiaColor.SUCCESS);
        embedBuilder.setTitle(title != null ? title : "성공");
        setDesc(desc, embedBuilder);
        setUser(user, embedBuilder);

        return embedBuilder;
    }

    @NotNull
    public static EmbedBuilder fail(@Nullable String title, @Nullable String desc, @Nullable User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(DiaColor.FAIL);
        embedBuilder.setTitle(title != null ? title : "실패");
        setDesc(desc, embedBuilder);
        setUser(user, embedBuilder);

        return embedBuilder;
    }

    @NotNull
    public static EmbedBuilder severe(@Nullable String title, @Nullable String desc, @Nullable User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(DiaColor.SEVERE);
        embedBuilder.setTitle(title != null ? title : "주의!");
        setDesc(desc, embedBuilder);
        setUser(user, embedBuilder);

        return embedBuilder;
    }

    private static void setTitle(@Nullable String title, EmbedBuilder embedBuilder) {
        if (title != null) embedBuilder.setTitle(title);
    }

    private static void setDesc(@Nullable String desc, EmbedBuilder embedBuilder) {
        if (desc != null) embedBuilder.setDescription(desc);
    }

    private static void setUser(@Nullable User user, EmbedBuilder embedBuilder) {
        if (user != null) embedBuilder.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());

        else embedBuilder.setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL);
    }

}
