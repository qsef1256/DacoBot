package net.qsef1256.dacobot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.qsef1256.dacobot.game.explosion.data.ItemTypeEntity;
import net.qsef1256.dacobot.game.explosion.model.Cash;
import net.qsef1256.dacobot.game.explosion.model.Inventory;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.qsef1256.dacobot.DacoBot.logger;

public class InventoryCommand extends SlashCommand {

    public InventoryCommand() {
        name = "인벤토리";
        help = "닦던마냥 16개만 있거나 그러지 않아요.";

        children = new SlashCommand[]{
                new SeeCommand(),
                new ItemInfoCommand(),
                new ItemAddCommand(),
                new ItemRemoveCommand()
        };

        subcommandGroup = new ItemCommandGroup();
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        event.reply("추가 명령어를 입력하세요! : " + getHelp()).queue();
    }

    private static class SeeCommand extends SlashCommand {

        public SeeCommand() {
            name = "보기";
            help = "인벤토리를 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                event.replyEmbeds(getInventoryEmbed(user).build()).queue();
            } catch (RuntimeException e) {
                logger.warn(e.getMessage());
                e.printStackTrace();
                event.replyEmbeds(DiaEmbed.error("인벤토리 로드 실패", null, e, user).build()).queue();
            }
        }

        @NotNull
        private EmbedBuilder getInventoryEmbed(@NotNull User user) {
            final Cash cash = new Cash(user.getIdLong());

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setColor(DiaColor.INFO)
                    .setTitle("%s의 인벤토리".formatted(user.getName()));

            StringBuilder items = new StringBuilder();
            Inventory inventory = Inventory.fromUser(user.getIdLong());

            inventory.getItems().forEach((id, item) -> {
                ItemTypeEntity itemType = item.getItemType();

                String itemInfo = "%s%s : %s > %s개".formatted(
                        itemType.getItemIcon() + " ", itemType.getItemName(), itemType.getItemRank(), item.getAmount());
                items.append(itemInfo);
                items.append("\n");
            });

            embedBuilder.addField("아이템 목록", items.toString(), false);
            embedBuilder.addField(":moneybag:돈", cash.getCash() + " 캐시", true);
            embedBuilder.addField(":gem:보유 다이아", cash.getPickaxeCount() + " 개", true);

            return embedBuilder;
        }
    }

    private static class ItemCommandGroup extends SubcommandGroupData {

        public ItemCommandGroup() {
            super("아이템", "아이템의 정보를 확인하거나 사용합니다.");

            // FIXME: fix command Data
            addSubcommands(SubcommandData.fromData(new ItemInfoCommand().getData()));
            addSubcommands(SubcommandData.fromData(new ItemAddCommand().getData()));
            addSubcommands(SubcommandData.fromData(new ItemRemoveCommand().getData()));
        }
    }

    private static class ItemInfoCommand extends SlashCommand {
        public ItemInfoCommand() {
            name = "정보";
            help = "아이템 정보를 확인합니다.";

            options = List.of(
                    new OptionData(OptionType.STRING, "이름", "아이템 이름")
            );
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            event.reply(DiaMessage.underConstruction()).queue();
        }

        @NotNull
        public DataObject getData() {
            return buildCommandData().toData();
        }
    }

    private static class ItemAddCommand extends SlashCommand {

        public ItemAddCommand() {
            name = "추가";
            help = "관리용 치트";
            ownerCommand = true;
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                Inventory.fromUser(user.getIdLong()).addItem(1);
            } catch (RuntimeException e) {
                e.printStackTrace();
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }
        }

        @NotNull
        public DataObject getData() {
            return buildCommandData().toData();
        }
    }

    private static class ItemRemoveCommand extends SlashCommand {

        public ItemRemoveCommand() {
            name = "삭제";
            help = "관리용 치트";
            ownerCommand = true;
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                Inventory.fromUser(user.getIdLong()).removeItem(1);
            } catch (RuntimeException e) {
                e.printStackTrace();
                event.replyEmbeds(DiaEmbed.error(null, null, e, user).build()).queue();
            }

        }

        @NotNull
        public DataObject getData() {
            return buildCommandData().toData();
        }
    }
}
