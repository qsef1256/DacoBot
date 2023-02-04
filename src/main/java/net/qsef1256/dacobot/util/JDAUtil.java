package net.qsef1256.dacobot.util;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dialib.util.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public class JDAUtil {

    /**
     * 해당 멤버가 해당 슬래시 명령어를 실행할 수 있는지 확인합니다.
     *
     * @param slashCommand slash command to check
     * @param member       executing member
     * @return true when member can execute
     */
    public boolean canExecute(@NotNull SlashCommand slashCommand, Member member) {
        if (slashCommand.isOwnerCommand()) {
            return DacoBot.getCommandClient().getOwnerIdLong() == member.getIdLong();
        }

        return CommonUtil.allContains(slashCommand.getUserPermissions(), member
                .getPermissions()
                .toArray(new Permission[0]));
    }

    public boolean canExecute(@NotNull Command command, Member member) {
        if (command.isOwnerCommand()) {
            return DacoBot.getCommandClient().getOwnerIdLong() == member.getIdLong();
        }

        return CommonUtil.allContains(command.getUserPermissions(), member
                .getPermissions()
                .toArray(new Permission[0]));
    }

    /**
     * 유저 이름을 `qsef1256#6620` 형식으로 얻습니다.
     *
     * @param userId User's snowflake id
     * @return User's Name | null if bot can't find name
     */
    @Nullable
    public String getNameAsTag(final long userId) {
        User user = DacoBot.getJda().getUserById(userId);
        if (user == null) return null;

        return user.getAsTag();
    }

    /**
     * 이름으로 부터 유저를 찾습니다. (대문자 구분)<br>
     * 여러 명이 있을 수 있습니다.
     *
     * @param userName User name (user.getName())
     * @return List of matching users
     */
    public List<User> getUserFromName(String userName) {
        return DacoBot.getJda().getUsersByName(userName, false);
    }

    /**
     * 태그에서 유저를 찾습니다.
     *
     * @param userTag `qsef1256#6620`
     * @return matching User
     */
    public User getUserFromTag(String userTag) {
        return DacoBot.getJda().getUserByTag(userTag);
    }

    /**
     * Snowflake 에서 유저를 찾습니다.
     *
     * @param userId User's snowflake id
     * @return matching User
     * @see #retrieveUserFromId(long)
     */
    public User getUserFromId(long userId) {
        return DacoBot.getJda().getUserById(userId);
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
        DacoBot.getJda().retrieveUserById(userId).queue(user::set);
        return user.get();
    }

    public Member getMemberFromUser(User user) {
        Guild guild = DacoBot.getJda().getGuildById(DiaSetting.getInstance().getMainGuildID()); // TODO: for sub guilds?

        if (guild == null) return null;
        return guild.getMember(user);
    }

    public Member getMemberFromId(long userId) {
        return getMemberFromUser((User) User.fromId(userId));
    }

    @Nullable
    public OptionMapping getOptionMapping(@NotNull SlashCommandEvent event, String optionName) {
        final OptionMapping option = event.getOption(optionName);
        if (option == null) {
            event.reply("%s를 입력해주세요.".formatted(optionName)).setEphemeral(true).queue();
            return null;
        }
        return option;
    }

}
