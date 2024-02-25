package net.qsef1256.dacobot.game.explosion.v2.cash;

import lombok.AllArgsConstructor;
import net.qsef1256.dacobot.game.explosion.domain.inventory.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CashService {

    private final CashRepository cash;
    private final UserService user;

    @NotNull
    private CashEntity getCashEntity(long discordId) {
        if (!cash.existsById(discordId)) createCash(discordId);

        return cash.getReferenceById(discordId);
    }

    public void createCash(long discordId) {
        CashEntity cashEntity = new CashEntity();
        cashEntity.setUser(user.getUser(discordId));

        cash.save(cashEntity);
    }

    public void deleteCash(long discordId) {
        cash.deleteById(discordId);
    }

    public long getCash(long discordId) {
        return getCashEntity(discordId).getCash();
    }

    public void changeCash(long discordId, long amount) {
        CashEntity cashEntity = getCashEntity(discordId);
        long currentCash = cashEntity.getCash();
        cashEntity.setCash(Math.max(0, currentCash + amount));

        cash.save(cashEntity);
    }

    public int getPickaxeCount(long discordId) {
        return getCashEntity(discordId).getPickaxeCount();
    }

    public void changePickaxeCount(long discordId, int count) {
        CashEntity cashEntity = getCashEntity(discordId);
        int currentCount = cashEntity.getPickaxeCount();
        cashEntity.setPickaxeCount(Math.max(0, currentCount + count));

        cash.save(cashEntity);
    }

}
