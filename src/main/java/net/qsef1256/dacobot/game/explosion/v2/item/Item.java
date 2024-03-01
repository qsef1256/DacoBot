package net.qsef1256.dacobot.game.explosion.v2.item;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeEntity;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @ManyToOne
    @JoinColumn(name = "item_type")
    private ItemTypeEntity type;
    @Positive
    private int amount = 1;

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void removeAmount(int amount) {
        this.amount -= amount;
    }

}
