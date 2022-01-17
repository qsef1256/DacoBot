package net.qsef1256.diabot.data;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DiscordUserData {

    private long discord_id;
    private Timestamp register_time;
    private String status;

    public DiscordUserData(final long discord_id, final Timestamp register_time, final String status) {
        if (discord_id == 0) {
            throw new IllegalArgumentException("discord_id can't be 0");
        }
        this.discord_id = discord_id;
        this.register_time = register_time;
        this.status = status;
    }

}
