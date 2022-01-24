package net.qsef1256.diabot.game.explosion.model;

import lombok.Getter;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.game.explosion.data.ExplosionCash;
import net.qsef1256.diabot.util.DiscordUtil;

import java.util.NoSuchElementException;

import static net.qsef1256.diabot.DiaBot.logger;

public class ExplosionUser {

    protected final DaoCommon<Long, ExplosionCash> dao = new DaoCommonImpl<>(ExplosionCash.class);
    @Getter
    private final ExplosionCash data;

    public ExplosionUser(final long discord_id) {
        try {
            data = dao.findById(discord_id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(DiscordUtil.getNameAsTag(discord_id) + "님의 계정이 존재하지 않습니다.");
        } catch (RuntimeException e) {
            logger.warn(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + "님의 계정을 로드하는데 실패했습니다.");
        }
    }

    public long getCash() {
        return data.getCash();
    }

    public void addCash(final int amount) {
        data.setCash(getCash() + amount);
        if (data.getCash() < 0) {
            data.setCash(0L);
        }
        dao.update(data);
    }

    public int getPickaxeCount() {
        return data.getPickaxeCount();
    }

    public void addPickaxeCount(final int count) {
        data.setPickaxeCount(getPickaxeCount() + count);
        if (data.getPickaxeCount() < 0) {
            data.setPickaxeCount(0);
        }
        dao.update(data);
    }

    public void addPickaxeCount() {
        addPickaxeCount(1);
    }

}
