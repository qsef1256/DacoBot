package net.qsef1256.dacobot.core.command;

import com.jagrosh.jdautilities.command.CommandClient;
import lombok.Getter;
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

}
