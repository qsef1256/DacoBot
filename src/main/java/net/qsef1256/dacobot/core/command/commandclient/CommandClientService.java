package net.qsef1256.dacobot.core.command.commandclient;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.ContextMenu;
import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.qsef1256.dialib.util.CommonUtil;
import net.qsef1256.dialib.util.TryUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
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

    public void upsertToGuild(@NotNull Guild guild) {
        log.info("Upsert command data for Guild id %s".formatted(guild.getId()));

        List<CommandData> commandDataList = commandClient.getSlashCommands()
                .stream()
                .map(tryRun(SlashCommand::buildCommandData))
                .toList();
        List<CommandData> contextMenuList = commandClient.getContextMenus()
                .stream()
                .map(tryRun(ContextMenu::buildCommandData))
                .toList();

        List<CommandData> allCommandData = new ArrayList<>();
        allCommandData.addAll(commandDataList);
        allCommandData.addAll(contextMenuList);

        guild.updateCommands()
                .addCommands(allCommandData)
                .queue();
    }

    @NotNull
    private <T> Function<T, CommandData> tryRun(@NotNull Function<T, CommandData> operation) {
        return TryUtil.run(operation, (e, slash) -> {
            throw new IllegalStateException("failed to build %s data"
                    .formatted(slash.getClass().getSimpleName()), e);
        });
    }

}
