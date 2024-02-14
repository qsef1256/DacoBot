package net.qsef1256.dacobot.core.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.qsef1256.dialib.util.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Getter
@Service
public class CommandClientService {

    private final CommandClient commandClient;

    public CommandClientService(@Lazy CommandClient commandClient) {
        this.commandClient = commandClient;
    }

    public boolean isOwner(long discordId) {
        return commandClient.getOwnerIdLong() == discordId;
    }

    public boolean isNotOwner(long discordId) {
        return !isOwner(discordId);
    }

    /**
     * 해당 멤버가 해당 슬래시 명령어를 실행할 수 있는지 확인합니다.
     *
     * @param slashCommand slash command to check
     * @param member       executing member
     * @return true when member can execute
     */
    public boolean canExecute(@NotNull SlashCommand slashCommand, Member member) {
        if (slashCommand.isOwnerCommand())
            return commandClient.getOwnerIdLong() == member.getIdLong();

        return CommonUtil.allContains(slashCommand.getUserPermissions(), member
                .getPermissions()
                .toArray(new Permission[0]));
    }

    public boolean canExecute(@NotNull Command command, Member member) {
        if (command.isOwnerCommand())
            return commandClient.getOwnerIdLong() == member.getIdLong();

        return CommonUtil.allContains(command.getUserPermissions(), member
                .getPermissions()
                .toArray(new Permission[0]));
    }

}
