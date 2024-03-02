package net.qsef1256.dacobot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.game.explosion.v2.inventory.InventoryController;
import net.qsef1256.dacobot.game.explosion.v2.inventory.InventoryService;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class InventoryCommand extends DacoCommand {

    public InventoryCommand(@NotNull SeeCommand seeCommand,
                            @NotNull ItemInfoCommand infoCommand,
                            @NotNull ItemAddCommand addCommand,
                            @NotNull ItemRemoveCommand removeCommand,
                            @NotNull ItemCommandGroup commandGroup) {
        name = "인벤토리";
        help = "닦던마냥 16개만 있거나 그러지 않아요.";
        children = new SlashCommand[]{
                seeCommand,
                infoCommand,
                addCommand,
                removeCommand
        };
        subcommandGroup = commandGroup;
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        event.reply("추가 명령어를 입력하세요! : " + getHelp()).queue();
    }

    @Component
    public static class SeeCommand extends DacoCommand {

        private final InventoryController inventory;

        public SeeCommand(@NotNull InventoryController inventory) {
            this.inventory = inventory;

            name = "보기";
            help = "인벤토리를 확인합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();
            try {
                event.replyEmbeds(inventory.getInventoryEmbed(user)).queue();
            } catch (RuntimeException e) {
                log.error("인벤토리 로드 실패", e);

                event.replyEmbeds(DiaEmbed.error("인벤토리 로드 실패", null, e, user).build()).queue();
            }
        }

    }

    @Component
    public static class ItemCommandGroup extends SubcommandGroupData {

        public ItemCommandGroup(@NotNull ItemInfoCommand infoCommand,
                                @NotNull ItemAddCommand addCommand,
                                @NotNull ItemRemoveCommand removeCommand) {
            super("아이템", "아이템의 정보를 확인하거나 사용합니다.");

            // FIXME: fix command Data
            addSubcommands(SubcommandData.fromData(infoCommand.getData()));
            addSubcommands(SubcommandData.fromData(addCommand.getData()));
            addSubcommands(SubcommandData.fromData(removeCommand.getData()));
        }

    }

    @Component
    public static class ItemInfoCommand extends DacoCommand {

        public ItemInfoCommand() {
            name = "정보";
            help = "아이템 정보를 확인합니다.";

            options = List.of(new OptionData(OptionType.STRING, "이름", "아이템 이름"));
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            callUnderConstruction();
        }

        @NotNull
        public DataObject getData() {
            return buildCommandData().toData();
        }

    }

    @Component
    public static class ItemAddCommand extends DacoCommand {

        private final InventoryService inventory;

        public ItemAddCommand(@NotNull InventoryService inventory) {
            this.inventory = inventory;

            name = "추가";
            help = "관리용 치트";
            ownerCommand = true;
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            inventory.addItem(user.getIdLong(), 1);
        }

        @NotNull
        public DataObject getData() {
            return buildCommandData().toData();
        }

    }

    @Component
    public static class ItemRemoveCommand extends DacoCommand {

        private final InventoryService inventory;

        public ItemRemoveCommand(@NotNull InventoryService inventory) {
            this.inventory = inventory;

            name = "삭제";
            help = "관리용 치트";
            ownerCommand = true;
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            inventory.removeItem(user.getIdLong(), 1);
        }

        @NotNull
        public DataObject getData() {
            return buildCommandData().toData();
        }

    }

}
