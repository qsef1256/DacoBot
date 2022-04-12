package net.qsef1256.dacobot.game.sudoku.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SudokuCommand extends SlashCommand {

    public SudokuCommand() {
        name = "스도쿠";
        help = "스도쿠 게임을 시작합니다.";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        // TODO
    }

}
