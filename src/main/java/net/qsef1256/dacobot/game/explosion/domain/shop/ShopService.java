package net.qsef1256.dacobot.game.explosion.domain.shop;

import net.qsef1256.dacobot.game.explosion.domain.item.ItemEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(@NotNull ShopRepository repository) {
        shopRepository = repository;
    }

    public long getPrice(@NotNull ItemEntity item) {
        ShopEntity shopEntity = shopRepository.getReferenceById(Long.valueOf(item.getId()));
        // FIXME: getReferenceById will throw exception when entity not found

        return shopEntity.getPrice();
    }

    public void buy(long discordId, @NotNull ItemEntity item) {
        // TODO
    }

    public void sell(long discordId, @NotNull ItemEntity item) {
        // TODO
    }

}
