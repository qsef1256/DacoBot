package net.qsef1256.diabot.game.explosion.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.game.explosion.data.ItemEntity;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExplosionItem {

    protected static final DaoCommon<Integer, ItemEntity> dao = new DaoCommonImpl<>(ItemEntity.class);
    @Getter
    private final ItemEntity data = new ItemEntity();

    public ExplosionItem(long item_id) {
        data.setItemID(item_id);
        data.setAmount(1L);
        data.setLastGetTime(LocalDateTime.now());
    }

    public ExplosionItem(int item_id, long amount) {
        this(item_id);
        data.setAmount(amount);
    }

}
