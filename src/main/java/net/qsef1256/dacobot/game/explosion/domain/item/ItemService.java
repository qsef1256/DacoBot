package net.qsef1256.dacobot.game.explosion.domain.item;

import org.springframework.stereotype.Service;

@Service
public class ItemService {

    // TODO

    public ItemOld getItem(int itemId) {
        return getItem(itemId, 1);
    }

    public ItemOld getItem(int itemId, int amount) {
        return ItemOld.fromId(itemId);
    }

    public ItemOld fromUser(Long userId, int itemId) {
        return ItemOld.fromUser(userId, itemId);
    }

}
