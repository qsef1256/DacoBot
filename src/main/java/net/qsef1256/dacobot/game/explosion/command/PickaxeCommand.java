package net.qsef1256.dacobot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.game.explosion.domain.cash.CashService;
import net.qsef1256.dialib.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@Component
public class PickaxeCommand extends SlashCommand {

    private final CashService cashService;

    public PickaxeCommand(@NotNull CashService cashService) {
        this.cashService = cashService;

        name = "곡괭이";
        help = "건드리면 폭탄 터트릴꺼에요...";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        User user = event.getUser();

        event.deferReply().queue(callback -> {
            try {
                String status;
                int pickaxeCount;
                int random = RandomUtil.randomInt(1, 100);

                switch (random) {
                    case 87, 88, 89 -> {
                        pickaxeCount = RandomUtil.randomInt(-5, -1);
                        status = ":crescent_moon: 어이쿠 손이 미끄러졌네!! 다이아가 박살났습니다. ";
                    }
                    case 90, 91, 92, 93, 94, 95, 96 -> {
                        pickaxeCount = RandomUtil.randomInt(2, 4);
                        status = ":pick: 깨진다... ";
                    }
                    case 97, 98, 99 -> {
                        pickaxeCount = RandomUtil.randomInt(5, 8);
                        status = ":sparkles: **치명타!** ";
                    }
                    case 100 -> {
                        pickaxeCount = RandomUtil.randomInt(15, 25);
                        status = ":boom: **폭발!!** ";
                    }
                    default -> {
                        pickaxeCount = 1;
                        status = ":gem: ";
                    }
                }

                cashService.changePickaxeCount(user.getIdLong(), pickaxeCount);

                String pickaxeCountDisplay = (pickaxeCount > 0) ? "+" + pickaxeCount : String.valueOf(pickaxeCount);
                callback.editOriginal("%s`%s` 다이아 보유량: `%d` 개".formatted(status,
                        pickaxeCountDisplay,
                        cashService.getPickaxeCount(user.getIdLong()))).queue();
            } catch (RuntimeException e) {
                String message = ":warning: " + user.getName() + " 는 손이 미끄러져 다이아를 캐지 못했습니다!\n\n오류: " + e.getMessage();

                if (e instanceof NoSuchElementException) {
                    message = message + "\n곡괭이 커맨드는 계정 등록이 있어야 사용 가능해요. `/계정 등록` 을 입력하세요.";
                } else {
                    log.error("can't add diamond pickaxe for " + user.getIdLong(), e);
                }
                callback.editOriginal(message).queue();
            }
        });

    }

}
