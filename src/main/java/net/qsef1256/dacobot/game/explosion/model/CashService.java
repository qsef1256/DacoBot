package net.qsef1256.dacobot.game.explosion.model;

import net.qsef1256.dacobot.game.explosion.data.CashEntity;
import net.qsef1256.dacobot.game.explosion.data.CashRepository;
import net.qsef1256.dacobot.module.account.controller.AccountController;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashService {

    private final AccountController accountController;
    private final CashRepository cashRepository;

    @Autowired
    public CashService(AccountController accountController,
                       CashRepository cashRepository) {
        this.accountController = accountController;
        this.cashRepository = cashRepository;
    }

    public CashEntity getCash(long discordId) {
        return cashRepository.findByDiscordUser_DiscordId(discordId)
                .orElseGet(() -> createCash(discordId));
    }

    @NotNull
    private CashEntity createCash(long discordId) {
        UserEntity account = accountController.getAccount(discordId);
        CashEntity cashEntity = new CashEntity().setDiscordUser(account);

        return cashRepository.save(cashEntity);
    }

    public void addCash(long discordId, int amount) {
        CashEntity cashEntity = getCash(discordId);
        long currentCash = cashEntity.getCash();
        cashEntity.setCash(Math.max(0, currentCash + amount));
        cashRepository.save(cashEntity);
    }

    public int getPickaxeCount(long discordId) {
        return getCash(discordId).getPickaxeCount();
    }

    public void changePickaxeCount(long discordId, int count) {
        CashEntity cashEntity = getCash(discordId);
        int currentCount = cashEntity.getPickaxeCount();
        cashEntity.setPickaxeCount(Math.max(0, currentCount + count));
        cashRepository.save(cashEntity);
    }

    public void addPickaxeCount(long discordId) {
        changePickaxeCount(discordId, 1);
    }

}