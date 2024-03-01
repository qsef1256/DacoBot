package net.qsef1256.dacobot.game.explosion.v2.shop;

import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(@NotNull ShopRepository repository) {
        shopRepository = repository;
    }

    public long getPrice(@NotNull ItemTypeEntity item) {
        ShopEntity shopEntity = shopRepository.getReferenceById(Long.valueOf(item.getItemId()));
        // FIXME: getReferenceById will throw exception when entity not found

        return shopEntity.getPrice();
    }

    public void buy(long discordId, @NotNull ItemTypeEntity item) {
        // TODO
    }

    public void sell(long discordId, @NotNull ItemTypeEntity item) {
        // TODO
    }

}
