package net.qsef1256.diabot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.diabot.game.explosion.model.ExplosionUser;
import net.qsef1256.diabot.util.CommonUtil;

import java.util.NoSuchElementException;

public class PickaxeCommand extends SlashCommand {

    public PickaxeCommand() {
        name = "곡괭이";
        help = "건드리면 폭탄 터트릴꺼에요...";
    }

    @Override
    protected void execute(final SlashCommandEvent event) {
        final User user = event.getUser();

        event.deferReply().queue(callback -> {
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
                    case 87, 88, 89 -> {
                        pickaxeCount = CommonUtil.randomInt(-5, -1);
                        status = ":crescent_moon: 어이쿠 손이 미끄러졌네!! 다이아가 박살났습니다. ";
                    }
                    case 90, 91, 92, 93, 94, 95, 96 -> {
                        pickaxeCount = CommonUtil.randomInt(2, 4);
                        status = ":pick: 깨진다... ";
                    }
                    case 97, 98, 99 -> {
                        pickaxeCount = CommonUtil.randomInt(5, 8);
                        status = ":sparkles: **치명타!** ";
                    }
                    case 100 -> {
                        pickaxeCount = CommonUtil.randomInt(15, 25);
                        status = ":boom: **폭발!!** ";
                    }
                }

                explosionUser.addPickaxeCount(pickaxeCount);

                String pickaxeCountDisplay = (pickaxeCount > 0) ? "+" + pickaxeCount : String.valueOf(pickaxeCount);
                callback.editOriginal(status + "`" + pickaxeCountDisplay + "` 다이아 보유량: `" + explosionUser.getPickaxeCount() + "` 개").queue();
            } catch (RuntimeException e) {
                String message = ":warning: " + user.getAsTag() + " 는 손이 미끄러져 다이아를 캐지 못했습니다!\n\n오류: " + e.getMessage();

                if (e instanceof NoSuchElementException) {
                    message = message + "\n곡괭이 커맨드는 계정 등록이 있어야 사용 가능해요. `/계정 등록` 을 입력하세요.";
                } else {
                    e.printStackTrace();
                }
                callback.editOriginal(message).queue();
            }
        });

    }
}
