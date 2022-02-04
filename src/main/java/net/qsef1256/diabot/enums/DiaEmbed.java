package net.qsef1256.diabot.enums;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
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
    public static EmbedBuilder error(@Nullable String title, @Nullable String desc, @Nullable Exception e, @Nullable User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(DiaColor.FAIL);
        embedBuilder.setTitle(title != null ? title : "오류 발생");
        if (desc != null) embedBuilder.setDescription(desc);
        if (user != null) embedBuilder.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
        else embedBuilder.setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL);
        if (e != null) embedBuilder.addField("오류 정보", e.getMessage(), false);
        embedBuilder.setFooter("문제가 계속될 시 관리자에게 연락 해주세요.");

        return embedBuilder;
    }

}
