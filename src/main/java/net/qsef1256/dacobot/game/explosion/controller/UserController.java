package net.qsef1256.dacobot.game.explosion.controller;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.game.explosion.v2.cash.CashEntity;
import net.qsef1256.dacobot.game.explosion.v2.cash.CashRepository;
import net.qsef1256.dacobot.game.explosion.v2.user.UserId;
import net.qsef1256.dacobot.module.account.entity.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;

import java.util.NoSuchElementException;

@Slf4j
@Controller
public class UserController { // TODO: is this service layer?

    private final JdaService jdaService;
    private final UserRepository user;
    private final CashRepository cash;

    public UserController(@NotNull JdaService jdaService,
                          @NotNull UserRepository user,
                          @NotNull CashRepository cash) {
        this.jdaService = jdaService;
        this.user = user;
        this.cash = cash;
    }

    @NotNull
    private UserId getUserId(long discordId) {
        return new UserId(user.getReferenceById(discordId));
    }

    // TODO: Is user-cash relation appropriate?
    public void register(long discordId) {
        try {
            if (!user.existsById(discordId))
                throw new NoSuchElementException(jdaService.getNameAsTag(discordId) + " 유저가 존재하지 않습니다.");
            CashEntity cashData = new CashEntity();
            cashData.setUser(getUserId(discordId));
            cashData.setCash(0L);
            cashData.setPickaxeCount(0);
            cash.saveAndFlush(cashData);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (final RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException(jdaService.getNameAsTag(discordId) + " 데이터 등록에 실패했습니다.");
        }
    }

    public void reset(long discordId) {
        try {
            cash.deleteById(getUserId(discordId));
            register(discordId);
        } catch (final RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException(jdaService.getNameAsTag(discordId) + " 초기화에 실패했습니다.");
        }
    }

    public boolean isExist(long discordId) {
        return cash.existsById(getUserId(discordId));
    }

}
