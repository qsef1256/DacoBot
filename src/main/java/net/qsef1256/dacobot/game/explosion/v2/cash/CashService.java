package net.qsef1256.dacobot.game.explosion.v2.cash;

import net.qsef1256.dacobot.game.explosion.v2.user.UserIdService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CashService {

    private final CashRepository cash;
    private final UserIdService userId;

    public CashService(@NotNull UserIdService userId,
                       @NotNull CashRepository cash) {
        this.userId = userId;
        this.cash = cash;
    }

    @NotNull
    private CashEntity getCashEntity(long discordId) {
        if (!cash.existsById(userId.getUserId(discordId))) createCash(discordId);

        return cash.getReferenceById(userId.getUserId(discordId));
    }

    private void createCash(long discordId) {
        CashEntity cashEntity = new CashEntity();
        cashEntity.setUser(userId.getUserId(discordId));
        cashEntity.setCash(0);

        cash.saveAndFlush(cashEntity);
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
