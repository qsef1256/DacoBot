package net.qsef1256.diabot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.diabot.game.explosion.model.ExplosionUser;
import net.qsef1256.diabot.util.CommonUtil;

import java.sql.SQLException;

public class PickaxeCommand extends SlashCommand {

    public PickaxeCommand() {
        name = "곡괭이";
        help = "건드리면 폭탄 터트릴꺼에요...";
    }

    @Override
    protected void execute(final SlashCommandEvent event) {
        final User user = event.getUser();

        try {
            final ExplosionUser explosionUser = new ExplosionUser(user.getIdLong());

            final String status;
            final int pickaxeCount;
            final int random = CommonUtil.randomInt(1, 100);

            switch (random) {
                default -> {
                    pickaxeCount = 1;
                    status = ":gem: ";
                }
                case 90, 91, 92, 93, 94, 95, 96 -> {
                    pickaxeCount = CommonUtil.randomInt(2, 3);
                    status = ":pick: 깨진다... ";
                }
                case 97, 98, 99 -> {
                    pickaxeCount = CommonUtil.randomInt(5, 7);
                    status = ":sparkles: **치명타!** ";
                }
                case 100 -> {
                    pickaxeCount = CommonUtil.randomInt(15, 20);
                    status = ":boom: **폭발!!** ";
                }
            }

            explosionUser.addPickaxeCount(pickaxeCount);
            event.reply(status + "다이아 보유량: `" + explosionUser.getPickaxeCount() + "` 개").queue();
        } catch (final SQLException e) {
            event.reply(user.getAsTag() + " 는 손이 미끄러져 다이아를 캐지 못했습니다!").queue();
            e.printStackTrace();
        }
    }
}
