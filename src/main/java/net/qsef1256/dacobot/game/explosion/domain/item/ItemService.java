package net.qsef1256.dacobot.game.explosion.domain.item;

import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemType;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeEntity;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemTypeRepository itemTypeRepository;

    public ItemService(@NotNull ItemRepository itemRepository,
                       @NotNull ItemTypeRepository itemTypeRepository) {
        this.itemRepository = itemRepository;
        this.itemTypeRepository = itemTypeRepository;
    }

    public Item getItem(int itemId) {
        return getItem(itemId, 1);
    }

    public Item getItem(int itemId, int amount) {
        ItemTypeEntity itemType = itemTypeRepository
                .findById((long) itemId)
                .orElseThrow(() -> new NoSuchElementException("unknown item id: " + itemId));

        return Item.builder()
                .id(itemId)
                .itemType(ItemType.fromEntity(itemType))
                .amount(amount)
                .lastGetTime(LocalDateTime.now())
                .build();
    }

    // TODO
    /*
    public Item fromUser(long userId, int itemId) {
        ItemEntity userItem = itemRepository.findById((int) userId).getItem(itemId);
        if (userItem == null || userItem.getAmount() == 0)
            throw new NoSuchElementException("%s 아이템을 보유하고 있지 않습니다.".formatted(item.getName()));

        item.setItemEntity(userItem);
        return item;
    }
    */

}
