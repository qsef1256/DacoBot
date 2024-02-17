package net.qsef1256.dacobot.game.explosion.v2.cash;

import net.qsef1256.dacobot.game.explosion.domain.inventory.UserService;
import net.qsef1256.dacobot.game.explosion.v2.user.UserId;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CashService {

    private final CashRepository cash;
    private final UserService user;

    public CashService(@NotNull UserService user,
                       @NotNull CashRepository cash) {
        this.user = user;
        this.cash = cash;
    }

    @NotNull
    private CashEntity getCashEntity(long discordId) {
        return cash.getReferenceById(new UserId(user.getUser(discordId)));
    }

    // FIXME: auto creating cash entity? ma---ybe need? Hmm... or Controller layer required?
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
