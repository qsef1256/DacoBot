package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.util.CommonUtil;
import net.qsef1256.dacobot.util.GenericUtil;
import net.qsef1256.dacobot.util.JDAUtil;
import net.qsef1256.dacobot.util.notification.DiaEmbed;
import net.qsef1256.dacobot.util.notification.DiaMessage;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static net.qsef1256.dacobot.DacoBot.logger;

public class HelpCommand extends SlashCommand {

    static final Map<String, Map<?, ?>> categories = new HashMap<>();
    static Map<String, Object> settings = new HashMap<>();
    static final Map<String, SlashCommand> slashCommandMap = new HashMap<>();

    static {
        try (InputStream in = HelpCommand.class.getClassLoader().getResourceAsStream("commands.yml")) {
            settings = new Yaml().load(in);
            initHelp(settings);
        } catch (IOException e) {
            logger.error("Failed to load commands.yml");
            e.printStackTrace();
        }
    }

    public static void initCommands() {
        for (SlashCommand command : DacoBot.getCommandClient().getSlashCommands()) {
            logger.debug("Loading command name: %s".formatted(command.getClass().getSimpleName()));
            slashCommandMap.put(command.getClass().getSimpleName(), command);
        }
    }

    public HelpCommand() {
        name = "도움말";
        help = "다이아 덩어리를 다루는 방법";

        children = new SlashCommand[]{
                new MainMenuCommand(),
                new FindCommand()
        };
    }

    private static void initHelp(@NotNull Map<?, ?> map) {
        if (map.containsKey("CATEGORIES")) {
            Map<?, ?> category = (Map<?, ?>) map.get("CATEGORIES");
            category.forEach((key, valueMap) -> {
                if (valueMap instanceof Map<?, ?> values) {
                    String title = Optional.ofNullable(values.get("TITLE").toString()).orElse("제목 없음");
                    logger.debug("Putting category: %s".formatted(title));
                    categories.put(title, values);

                    if (values.get("CATEGORIES") instanceof Map<?, ?>) {
                        initHelp(values);
                    }
                }
            });
        }
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        SlashCommand[] children = getChildren();

        event.reply(DiaMessage.needSubCommand(children, event.getMember())).queue();
    }

    private static class MainMenuCommand extends SlashCommand {

        public MainMenuCommand() {
            name = "전체";
            help = "도움말의 메인 카테고리를 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            Member member = event.getMember();
            if (member == null) return;

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL);

            try {
                embedBuilder.setColor(DiaColor.MAIN_COLOR);
                String footer = CommonUtil.getRandomElement(
                        Arrays.asList("카테고리 만드는 거 힘들어 죽겠네...", "설명하기 귀차나. 일 안해", "정 모르겠으면 본체를 두들겨 보세요.... 잘하면 알려줄지도?"));
                embedBuilder.setFooter(footer);

                buildEmbed(embedBuilder, settings, member);
                event.replyEmbeds(embedBuilder.build())
                        .queue();
            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error(null, "설정 파일을 로드하는데 실패했습니다.", null, null).build()).queue();
                e.printStackTrace();
            }
        }
    }

    private static class FindCommand extends SlashCommand {

        public FindCommand() {
            name = "찾기";
            help = "특정 카테고리의 도움말을 찾습니다.";

            options = Collections.singletonList(new OptionData(OptionType.STRING, "카테고리", "찾을 카테고리").setRequired(true));
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            Member member = event.getMember();
            if (member == null) return;

            final OptionMapping option = event.getOption("카테고리");
            if (option == null) {
                event.reply("카테고리를 입력해주세요.").setEphemeral(true).queue();
                return;
            }
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL);

            Map<?, ?> map = categories.get(option.getAsString());
            if (map == null) {
                event.replyEmbeds(embedBuilder
                        .setTitle("찾기 실패")
                        .setColor(DiaColor.FAIL)
                        .setDescription(option.getAsString() + " 카테고리는 존재하지 않아요!")
                        .setFooter("/도움말 전체 로 카테고리 목록을 확인하세요.")
                        .build()).queue();
                return;
            }

            try {
                buildEmbed(embedBuilder, map, member);
                event.replyEmbeds(embedBuilder.build()).queue();
            } catch (RuntimeException e) {
                logger.warn(e.getMessage());
                event.replyEmbeds(DiaEmbed.error(null, option.getAsString() + " 카테고리 로딩중 오류가 발생했어요.", null, null)
                        .build()).queue();
            }
        }
    }

    private static void buildEmbed(@NotNull EmbedBuilder embedBuilder, @NotNull Map<?, ?> map, Member member) {
        if (map.get("TITLE") != null) {
            embedBuilder.setTitle(map.get("TITLE").toString());
        }
        if (map.get("DESC") != null) {
            embedBuilder.setDescription(map.get("DESC").toString());
        }

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
            if (!JDAUtil.canExecute(command, member)) return;
            String commandName = command.getName();
            String commandHelp = command.getHelp();
            embedBuilder.addField("/" + commandName, commandHelp, false);
        });

        embedBuilder.setColor(DiaColor.MAIN_COLOR);
    }

}
