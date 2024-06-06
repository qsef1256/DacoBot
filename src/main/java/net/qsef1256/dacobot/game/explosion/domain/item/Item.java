package net.qsef1256.dacobot.game.explosion.domain.item;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeEntity;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_type")
    private ItemTypeEntity type;

    @Positive
    private long amount = 1;

    public void addAmount(long amount) {
        this.amount += amount;
    }

    public void removeAmount(long amount) {
        this.amount -= amount;
    }

}
