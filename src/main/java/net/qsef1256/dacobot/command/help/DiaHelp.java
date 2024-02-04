package net.qsef1256.dacobot.command.help;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dialib.util.GenericUtil;
import net.qsef1256.dialib.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Component
public class DiaHelp {

    private final JdaService jdaService;
    private final CommandClient commandClient;

    private final Map<String, Map<?, ?>> categories = new HashMap<>();
    private Map<String, Object> settings = new HashMap<>();
    private final Map<String, SlashCommand> slashCommandMap = new HashMap<>();

    public DiaHelp(@NotNull JdaService jdaService,
                   @NotNull CommandClient commandClient) {
        this.jdaService = jdaService;
        this.commandClient = commandClient;

        load();
    }

    public void load() {
        try (InputStream in = HelpCommand.class
                .getClassLoader()
                .getResourceAsStream("commands.yml")) {
            settings = new Yaml().load(in);
            initHelp(settings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load commands.yml", e);
        }

        for (SlashCommand command : commandClient.getSlashCommands()) {
            log.debug("Loading command name: %s".formatted(command.getClass().getSimpleName()));
            slashCommandMap.put(command.getClass().getSimpleName(), command);
        }
    }

    private void initHelp(@NotNull Map<?, ?> map) {
        if (map.containsKey("CATEGORIES")) {
            Map<?, ?> category = (Map<?, ?>) map.get("CATEGORIES");
            category.forEach((key, valueMap) -> {
                if (valueMap instanceof Map<?, ?> values) {
                    String title = Optional.ofNullable(values.get("TITLE").toString()).orElse("제목 없음");
                    log.debug("Putting category: %s".formatted(title));
                    categories.put(title, values);

                    if (values.get("CATEGORIES") instanceof Map<?, ?>) {
                        initHelp(values);
                    }
                }
            });
        }
    }

    public EmbedBuilder getMainMenu(@NotNull Member member) {
        EmbedBuilder embedBuilder = buildEmbed(settings, member);
        embedBuilder.setColor(DiaColor.MAIN_COLOR);
        String footer = RandomUtil.getRandomElement(Arrays.asList(
                "카테고리 만드는 거 힘들어 죽겠네...",
                "설명하기 귀차나. 일 안해",
                "정 모르겠으면 본체를 두들겨 보세요.... 잘하면 알려줄지도?"));
        embedBuilder.setFooter(footer);

        return embedBuilder;
    }

    public EmbedBuilder getSearchResult(@NotNull Member member,
                                        @NotNull String search) {
        Map<?, ?> map = categories.get(search);
        if (map == null)
            throw new NoSuchElementException("can't find search result with " + search);

        return buildEmbed(map, member);
    }

    @NotNull
    private EmbedBuilder buildEmbed(@NotNull Map<?, ?> map,
                                    @NotNull Member member) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL);

        if (map.get("TITLE") != null)
            embedBuilder.setTitle(map.get("TITLE").toString());
        if (map.get("DESC") != null)
            embedBuilder.setDescription(map.get("DESC").toString());

        if (map.containsKey("CATEGORIES")) {
            Map<?, ?> category = (Map<?, ?>) map.get("CATEGORIES");
            category.forEach((key, value) -> {
                if (value instanceof Map<?, ?> values) {
                    String subTitle = Optional.ofNullable(values.get("TITLE").toString()).orElse("제목 없음");
                    String subDesc = Optional.ofNullable(values.get("DESC").toString()).orElse("내용 없음");
                    embedBuilder.addField("-" + subTitle, subDesc, false);
                }
            });
        }

        ArrayList<?> list = GenericUtil.getArrayList(map.get("LIST"));
        list.forEach(className -> {
            SlashCommand command = slashCommandMap.get(className.toString());
            if (!jdaService.canExecute(command, member)) return;
            String commandName = command.getName();
            String commandHelp = command.getHelp();
            embedBuilder.addField("/" + commandName, commandHelp, false);
        });

        embedBuilder.setColor(DiaColor.MAIN_COLOR);

        return embedBuilder;
    }

}
