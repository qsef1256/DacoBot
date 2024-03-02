package net.qsef1256.dacobot.game.explosion.v2.inventory;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.game.explosion.v2.cash.CashService;
import net.qsef1256.dacobot.game.explosion.v2.item.ItemDto;
import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeDto;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventory;
    private final CashService cashService;

    public MessageEmbed getInventoryEmbed(@NotNull User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                .setColor(DiaColor.INFO)
                .setTitle("%s의 인벤토리".formatted(user.getName()));

        StringBuilder items = new StringBuilder();
        inventory.getItems(user.getIdLong()).forEach(item -> {
            ItemTypeDto itemType = item.type();

            String itemInfo = "%s %s : %s > %s개".formatted(
                    itemType.itemIcon(),
                    itemType.itemName(),
                    itemType.itemRank(),
                    item.amount());
            items.append(itemInfo);
            items.append("\n");
        });

        embedBuilder.addField("아이템 목록", items.toString(), false);
        embedBuilder.addField(":moneybag:돈",
                "%d 캐시".formatted(cashService.getCash(user.getIdLong())), true);

        ItemDto item = inventory.getItem(user.getIdLong(), 2);
        embedBuilder.addField(":gem:보유 다이아", "%d 개".formatted(item.amount()), true);

        return embedBuilder.build();
    }

}
