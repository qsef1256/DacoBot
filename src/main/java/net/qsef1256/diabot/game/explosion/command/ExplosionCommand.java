package net.qsef1256.diabot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ExplosionCommand extends SlashCommand {

    public ExplosionCommand() {
        name = "폭발";
        help = "폭발은 예술이다...";

        //children = new SlashCommand[]{};
    }

    @Override
    protected void execute(final SlashCommandEvent event) {
        event.reply("준비 중! : " + getHelp()).queue();
    }

}

/*
private void displayHelp(Map<?, ?> categories) {
        if (categories.containsKey("CATEGORIES")) {
            Map<?, ?> category = (Map<?, ?>) categories.get("CATEGORIES");
            category.forEach((key, value) -> {
                if (value instanceof Map<?, ?> values) {
                    String title = Optional.ofNullable(values.get("TITLE").toString()).orElse("제목 없음");
                    String desc = Optional.ofNullable(values.get("TITLE").toString()).orElse("제목 없음");
                    ArrayList<?> list = getArrayList(values.get("LIST"));
                    System.out.println(title);
                    System.out.println(desc);
                    System.out.println(list);
                    if (values.get("CATEGORIES") instanceof Map<?, ?>) {
                        displayHelp(values);
                    }
                }
            });
        }
    }

    private ArrayList<?> getArrayList(Object toCheck) {
        ArrayList<?> list;
        if (toCheck instanceof ArrayList<?>) {
            list = (ArrayList<?>) toCheck;
        } else {
            list = new ArrayList<>();
        }
        return list;
    }
 */