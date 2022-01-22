package net.qsef1256.diabot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.diabot.DiaBot;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.enums.DiaImage;
import net.qsef1256.diabot.enums.DiaInfo;
import net.qsef1256.diabot.util.CommonUtil;

import java.util.Arrays;

public class HelpCommand extends SlashCommand {

    public HelpCommand() {
        name = "도움말";
        help = "다이아 덩어리를 다루는 방법";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (event.getMember() == null) return;

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(DiaColor.MAIN_COLOR);
        embedBuilder.setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL);
        for (SlashCommand slashCommand : DiaBot.getCommandClient().getSlashCommands()) {
            if (slashCommand.isOwner(event, DiaBot.getCommandClient()) || canExecute(slashCommand, event.getMember())) {
                embedBuilder.addField(" - " + slashCommand.getName(), slashCommand.getHelp(), false);
            }
        }

        String footer = CommonUtil.getRandomElement(
                Arrays.asList("카테고리 보여주기 귀찮으니깐, 자세한 설명은 직접 쳐서 알아보라구요.", "설명하기 귀차나. 일 안해", "정 모르겠으면 본체를 두들겨 보세요.... 잘하면 알려줄지도?"));
        embedBuilder.setFooter(footer);
        event.replyEmbeds(embedBuilder.build()).queue();
    }

    private boolean canExecute(SlashCommand slashCommand, Member member) {
        if (slashCommand.isOwnerCommand()) {
            return false;
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
