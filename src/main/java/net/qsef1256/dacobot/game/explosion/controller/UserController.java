package net.qsef1256.dacobot.game.explosion.controller;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.game.explosion.data.CashEntity;
import net.qsef1256.dacobot.module.account.data.UserEntity;
import net.qsef1256.dacobot.util.JDAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@Component
public class UserController {

    private final DaoCommonJpa<UserEntity, Long> mainDao = new DaoCommonJpaImpl<>(UserEntity.class);
    private final DaoCommonJpa<CashEntity, Long> cashDao = new DaoCommonJpaImpl<>(CashEntity.class);
    private final JDAService jdaService;

    @Autowired
    public UserController(JDAService jdaService) {
        this.jdaService = jdaService;
    }

    public void register(final long discord_id) {
        try {
            mainDao.open();
            cashDao.open();

            if (!mainDao.existsById(discord_id))
                throw new NoSuchElementException(jdaService.getNameAsTag(discord_id) + " 유저가 존재하지 않습니다.");
            CashEntity cashData = new CashEntity();
            cashData.setDiscordUser(mainDao.findById(discord_id));
            cashData.setCash(0L);
            cashData.setPickaxeCount(0);
            cashData.setPrestigeCount(0);
            cashDao.saveAndClose(cashData);
            mainDao.close();
        } catch (NoSuchElementException e) {
            throw e;
        } catch (final RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException(jdaService.getNameAsTag(discord_id) + " 데이터 등록에 실패했습니다.");
        }
    }

    public void reset(final long discord_id) {
        try {
            cashDao.open();
            cashDao.deleteById(discord_id);
            cashDao.close();
            register(discord_id);
        } catch (final RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException(jdaService.getNameAsTag(discord_id) + " 초기화에 실패했습니다.");
        }
    }

    public boolean isExist(long discordId) {
        return cashDao.existsById(discordId);
    }

}
