package net.qsef1256.dacobot.game.explosion.v2.cash;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.command.DacoCommand;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CashCommand extends DacoCommand {

    public CashCommand(@NotNull SeeCommand seeCommand,
                       @NotNull AddCommand addCommand,
                       @NotNull RemoveCommand removeCommand) {
        name = "돈";
        help = "돈 관리용 명령어";

        children = new SlashCommand[]{
                seeCommand,
                addCommand,
                removeCommand
        };
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

    @Component
    public static class SeeCommand extends DacoCommand {

        private final CashService cash;

        public SeeCommand(@NotNull CashService cash) {
            this.cash = cash;

            name = "조회";
            help = "돈 목록을 조회합니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            event.replyEmbeds(DiaEmbed.info("돈 목록",
                    "돈: " + cash.getCash(user.getIdLong()),
                    user).build()).queue();
        }

    }

    @Component
    public static class AddCommand extends DacoCommand {

        private final CashService cash;

        public AddCommand(@NotNull CashService cash) {
            this.cash = cash;

            name = "추가";
            help = "돈을 추가합니다.";
            options = List.of(
                    new OptionData(OptionType.INTEGER, "숫자", "숫자", true),
                    new OptionData(OptionType.USER, "유저", "유저", false)
            );

            ownerCommand = true;
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = getOptionUser("유저", event.getUser());
            Long option = getOptionLong("숫자");

            cash.changeCash(user.getIdLong(), option);
            event.replyEmbeds(DiaEmbed.info("돈 목록",
                    "돈: " + cash.getCash(user.getIdLong()),
                    user).build()).queue();
        }

    }

    @Component
    public static class RemoveCommand extends DacoCommand {

        private final CashService cash;

        public RemoveCommand(@NotNull CashService cash) {
            this.cash = cash;

            name = "제거";
            help = "돈을 제거합니다.";
            options = List.of(
                    new OptionData(OptionType.INTEGER, "숫자", "숫자", true),
                    new OptionData(OptionType.USER, "유저", "유저")
            );

            ownerCommand = true;
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = getOptionUser("유저", event.getUser());
            Long option = getOptionLong("숫자");

            cash.changeCash(user.getIdLong(), -option);
            event.replyEmbeds(DiaEmbed.info("돈 목록",
                    "돈: " + cash.getCash(user.getIdLong()),
                    user).build()).queue();
        }

    }

}
