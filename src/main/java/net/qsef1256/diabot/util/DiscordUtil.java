package net.qsef1256.diabot.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.diabot.DiaBot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@UtilityClass
public class DiscordUtil {

    /**
     * 유저 이름을 `qsef1256#6620` 형식으로 얻습니다.
     *
     * @param discord_id User's snowflake
     * @return User's Name | null if bot can't find name
     */
    @Nullable
    public String getNameAsTag(final long discord_id) {
        User user = DiaBot.getJda().getUserById(discord_id);
        if (user == null) return null;

        return user.getAsTag();
    }

    /**
     * 이름으로 부터 유저를 찾습니다. (대문자 구분)<br>
     * 여러 명이 있을 수 있습니다.
     * @param userName User name (user.getName())
     * @return List of matching users
     */
    public List<User> getUserFromName(String userName) {
        return DiaBot.getJda().getUsersByName(userName,false);
    }

    /**
     * 태그에서 유저를 찾습니다.
     * @param userTag `qsef1256#6620`
     * @return matching User
     */
    public User getUserFromTag(String userTag) {
        return DiaBot.getJda().getUserByTag(userTag);
    }

}
