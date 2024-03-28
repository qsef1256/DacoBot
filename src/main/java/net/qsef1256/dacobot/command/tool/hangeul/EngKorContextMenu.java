package net.qsef1256.dacobot.command.tool.hangeul;

import com.jagrosh.jdautilities.command.MessageContextMenu;
import com.jagrosh.jdautilities.command.MessageContextMenuEvent;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;

public class EngKorContextMenu extends MessageContextMenu {

    public EngKorContextMenu() {
        name = "영한 오타 번역기";
    }

    @Override
    protected void execute(@NotNull MessageContextMenuEvent event) {
        String result = KorEngConverter.getInstance().engToKor(event.getTarget().getContentRaw());

        MessageEmbed embed = DiaEmbed.info("영한 오타 번역", "결과: %s".formatted(result), null).build();
        event.reply(new MessageCreateBuilder().addEmbeds(embed).build()).queue();
    }

}
