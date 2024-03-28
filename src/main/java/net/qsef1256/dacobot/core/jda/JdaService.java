package net.qsef1256.dacobot.core.jda;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.qsef1256.dacobot.setting.DiaSetting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class JdaService {

    private final DiaSetting setting;
    @Getter
    private final JDA jda;

    public JdaService(@Lazy JDA jda,
                      @Lazy DiaSetting setting) {
        this.jda = jda;
        this.setting = setting;
    }

    /**
     * 유저 이름을 `qsef1256#6620` 형식으로 얻습니다.
     *
     * @param userId User's snowflake id
     * @return User's Name | null if bot can't find name
     */
    @Nullable
    public String getNameAsTag(long userId) {
        User user = jda.getUserById(userId);
        if (user == null) return null;

        return user.getName();
    }

    /**
     * 이름으로 부터 유저를 찾습니다. (대문자 구분)<br>
     * 여러 명이 있을 수 있습니다.
     *
     * @param userName User name (user.getName())
     * @return List of matching users
     */
    public List<User> getUserFromName(String userName) {
        return jda.getUsersByName(userName, false);
    }

    /**
     * Snowflake 에서 유저를 찾습니다.
     *
     * @param userId User's snowflake id
     * @return matching User
     * @see #retrieveUserFromId(long)
     */
    public User getUserFromId(long userId) {
        return jda.getUserById(userId);
    }

    /**
     * {@link #getUserFromId(long)} 와 같이 작동하지만, 캐시되지 않은 경우 디스코드 API 에서 유저를 찾습니다.
     *
     * <p><b>주의:</b> 비동기로 작동되어야 합니다.</p>
     *
     * @param userId User's snowflake id
     * @return matching user
     * @see #getUserFromId(long)
     */
    @Nullable
    public User retrieveUserFromId(long userId) {
        AtomicReference<User> user = new AtomicReference<>();
        jda.retrieveUserById(userId).queue(user::set);

        return user.get();
    }

    public Member getMemberFromUser(@NotNull User user) {
        Guild guild = getMainGuild(); // TODO: for sub guilds?

        if (guild == null) return null;
        return guild.getMember(user);
    }

    public Member getMemberFromId(long userId) {
        return getMemberFromUser((User) User.fromId(userId));
    }

    public Guild getMainGuild() {
        return jda.getGuildById(setting.getMainGuildID());
    }

    public MessageChannel getMainChannel() {
        return getMainGuild().getTextChannelById(setting.getMainChannelId());
    }
    
    @NotNull
    public List<Guild> getAllGuilds() {
        List<Guild> guilds = new ArrayList<>();

        guilds.add(getMainGuild());
        for (String subGuildId : setting.getSetting()
                .getString("bot.subGuildIds")
                .split(",\\s*")) {
            guilds.add(jda.getGuildById(subGuildId));
        }

        return guilds;
    }

    public List<Guild> getGuilds() {
        return jda.getGuilds();
    }

    public List<User> getUsers() {
        return jda.getUsers();
    }

}
