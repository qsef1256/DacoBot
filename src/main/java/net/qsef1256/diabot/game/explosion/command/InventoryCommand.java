package net.qsef1256.diabot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.game.explosion.data.ItemTypeEntity;
import net.qsef1256.diabot.game.explosion.model.Inventory;
import org.jetbrains.annotations.NotNull;

public class InventoryCommand extends SlashCommand {

    public InventoryCommand() {
        name = "인벤토리";
        help = "닦던마냥 16개만 있거나 그러지 않아요.";

        children = new SlashCommand[]{
                new SeeCommand(),
                new ItemCommand()
        };
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.reply("추가 명령어를 입력하세요! : " + getHelp()).queue();
    }

    public static class SeeCommand extends SlashCommand {

        public SeeCommand() {
            name = "보기";
            help = "인벤토리를 확인합니다.";
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            User user = event.getUser();

            event.replyEmbeds(getEmbedBuilder(user).build()).queue();
        }

        @NotNull
        private EmbedBuilder getEmbedBuilder(User user) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setColor(DiaColor.INFO)
                    .setTitle("%s의 인벤토리".formatted(user.getName()));

            new Inventory(user.getIdLong()).getItems().forEach((id, item) -> {
                ItemTypeEntity itemType = item.getItemType();
                

            });

            return embedBuilder;
        }
    }

    public static class ItemCommand extends SlashCommand {

        public ItemCommand() {
            name = "아이템";
            help = "아이템의 정보를 확인하거나 사용합니다.";
        }

        @Override
        protected void execute(SlashCommandEvent event) {

        }
    }

}
