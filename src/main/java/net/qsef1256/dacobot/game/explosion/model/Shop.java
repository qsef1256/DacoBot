package net.qsef1256.dacobot.game.explosion.model;

import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonHibernateImpl;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.game.explosion.data.ShopEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Shop {

    protected static final DaoCommonJpa<ShopEntity, Integer> dao = new DaoCommonJpaImpl<>(ShopEntity.class);

    public static long getPrice(@NotNull Item item) {
        Map<String, Object> constraint = new HashMap<>();
        constraint.put("ownerId", item.getItemId());
        ShopEntity shopEntity = dao.findBy(constraint).get(0);

        return shopEntity.getPrice();
    }

    public void buy(long discord_id, Item item) {

    }

    public void sell(long discord_id, Item item) {

    }

}
