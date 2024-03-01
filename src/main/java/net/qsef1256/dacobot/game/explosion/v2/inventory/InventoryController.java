package net.qsef1256.dacobot.game.explosion.v2.inventory;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.game.explosion.v2.cash.CashService;
import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeEntity;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
@Transactional
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
            ItemTypeEntity itemType = item.getType();

            String itemInfo = "%s %s : %s > %s개".formatted(
                    itemType.getItemIcon() != null ? itemType.getItemIcon() : "",
                    itemType.getItemName(),
                    itemType.getItemRank(),
                    item.getAmount());
            items.append(itemInfo);
            items.append("\n");
        });

        embedBuilder.addField("아이템 목록", items.toString(), false);
        embedBuilder.addField(":moneybag:돈",
                cashService.getCash(user.getIdLong()) + " 캐시", true);
        embedBuilder.addField(":gem:보유 다이아",
                cashService.getPickaxeCount(user.getIdLong()) + " 개", true);

        return embedBuilder.build();
    }

}
