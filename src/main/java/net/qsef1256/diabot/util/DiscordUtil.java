package net.qsef1256.diabot.util;

import net.dv8tion.jda.api.entities.User;
import net.qsef1256.diabot.DiaBot;
import org.jetbrains.annotations.Nullable;

public class DiscordUtil {

    /**
     * 유저 이름을 `qsef1256#6620` 형식으로 얻습니다.
     * @param discord_id User's snowflake
     * @return User's Name | null if bot can't find name
     */
    @Nullable
    public static String getNameAsTag(final long discord_id) {
        User user = DiaBot.getJda().getUserById(discord_id);
        if (user == null) return null;

        return user.getAsTag();
    }

}
