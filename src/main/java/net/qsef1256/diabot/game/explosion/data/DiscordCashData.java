package net.qsef1256.diabot.game.explosion.data;

import lombok.Data;

@Data
public class DiscordCashData {

    private long discord_id;
    private long cash;
    private int pickaxe_count;

    public DiscordCashData(final long discord_id, final long cash, final int pickaxe_count) {
        if (discord_id == 0) {
            throw new IllegalArgumentException("discord_id can't be 0");
        }
        this.discord_id = discord_id;
        this.cash = cash;
        this.pickaxe_count = pickaxe_count;
    }

}
