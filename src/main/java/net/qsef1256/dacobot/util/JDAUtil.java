package net.qsef1256.dacobot.util;

import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.qsef1256.dacobot.DacoBot;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@UtilityClass
public class JDAUtil {

    public static boolean canExecute(@NotNull SlashCommand slashCommand, Member member) {
        if (slashCommand.isOwnerCommand()) {
            return DacoBot.getCommandClient().getOwnerIdLong() == member.getIdLong();
        }
        if (slashCommand.getEnabledRoles().length != 0) {
            for (Role role : member.getRoles()) {
                if (Arrays.asList(slashCommand.getEnabledRoles()).contains(role.getId())) return true;
            }
            return false;
        }
        if (slashCommand.getEnabledUsers().length != 0) {
            return Arrays.asList(slashCommand.getEnabledUsers()).contains(member.getId());
        }
        return true;
    }

}
