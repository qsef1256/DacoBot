package net.qsef1256.dacobot.game.sudoku.model;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.system.snowflake.Messaged;

import java.util.HashMap;
import java.util.Map;

// TODO
public class SudokuManager extends Messaged {

    public static Map<User, SudokuGame> gameMap = new HashMap<>();

    public void start(User user) {
        gameMap.put(user, new SudokuGame());
    }

    @Override
    public String getMessageType() {
        return "sudoku";
    }

    @Override
    public Message getMessage() {
        return new MessageBuilder()
                .setEmbeds(new EmbedBuilder()
                        .setTitle("테스트")
                        .addField("테스트1", "스도쿠 들어갈 곳", false)
                        .build())
                .build();
    }

    @Override
    public Long getDiscordId() {
        return null;
    }

}
