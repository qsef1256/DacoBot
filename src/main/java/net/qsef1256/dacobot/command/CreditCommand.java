package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.enums.*;
import net.qsef1256.dacobot.util.CommonUtil;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Properties;

public class CreditCommand extends SlashCommand {

    public CreditCommand() {
        name = "정보";
        help = "다이아 덩어리의 구성 성분";

        children = new SlashCommand[]{
                new MainInfoCommand(),
                new LibraryCommand()
        };
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        SlashCommand[] children = getChildren();

        event.reply(DiaMessage.needSubCommand(children, event.getMember())).queue();
    }

    private static class MainInfoCommand extends SlashCommand {

        public MainInfoCommand() {
            name = "보기";
            help = "전반적인 정보를 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            Properties properties = null;

            try {
                properties = DacoBot.getProperties("project.properties");
            } catch (final IOException | RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error("정보 확인 실패", "봇 정보 확인에 실패했습니다.", null, null).build()).queue();
                e.printStackTrace();
            }

            if (properties == null) return;

            final long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
            final String message = CommonUtil.getRandomElement(
                    Arrays.asList("폭발은 예술이다!", "흠...", "연락처는 장식이다 카더라", "(할말 없음)", "멘트 추천은 본체한테 DM", "나는 댕청하다, /댕청"));

            final String formattedUptime = DurationFormatUtils.formatDurationHMS(uptime);
            final String name = properties.getProperty("name");
            final String version = properties.getProperty("version");

            final int serverSize = DacoBot.getJda().getGuilds().size();
            final int userSize = DacoBot.getJda().getUsers().size();

            event.replyEmbeds(new EmbedBuilder()
                    .setColor(DiaColor.MAIN_COLOR)
                    .setTitle(name + " Credits")
                    .setDescription("본체에게서 떨어져 나온 다이아 덩어리")
                    .setThumbnail(DiaImage.MAIN_THUMBNAIL)
                    .addField("본체", String.join(", ", DiaInfo.AUTHOR), true)
                    .addField("버전", "v" + version, true)
                    .addField("시작일", DiaInfo.SINCE, true)
                    .addField("언어", "Java", true)
                    .addField("서버", serverSize + "개", true)
                    .addField("유저", userSize + "명", true)
                    .addField("연락처", "`qsef1256@naver.com`", true)
                    .addField("가동 시간", formattedUptime, true)
                    .addField("", message, false)
                    .setFooter("provided by JDA v4.4.0_350")
                    .build()).queue();
        }

    }

    private static class LibraryCommand extends SlashCommand {

        public LibraryCommand() {
            name = "라이브러리";
            help = "사용 중인 라이브러리 & 라이센스 목록을 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(DiaColor.MAIN_COLOR)
                    .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                    .setTitle("라이브러리")
                    .addField("코어 라이브러리", """
                            [JDA](https://github.com/DV8FromTheWorld/JDA): `Apache-2.0`
                            [Chewtils](https://github.com/Chew/JDA-Chewtils): `Apache-2.0`
                            """, false)
                    .addField("DB 라이브러리", """
                            [Hibernate ORM](https://hibernate.org/orm/): `LGPL-2.1`
                            [HikariCP](https://github.com/brettwooldridge/HikariCP): `Apache-2.0`
                            [MariaDB Connector/J](https://mariadb.com/kb/en/mariadb-connector-j/): `LGPL-2.1-or-later`
                            [Spring Data JPA](https://spring.io/projects/spring-data-jpa): `Apache-2.0`
                            [Querydsl JPA](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa): `Apache-2.0`
                            """, false)
                    .addField("유틸 라이브러리", """
                            [Lombok](https://projectlombok.org/): `MIT`
                            [Apache Commons Lang3](https://github.com/apache/commons-lang): `Apache-2.0`
                            [Guava](https://github.com/google/guava): `Apache-2.0`
                            [Gson](https://github.com/google/gson): `Apache-2.0`
                            [SnakeYAML](https://bitbucket.org/snakeyaml/snakeyaml/src/master/): `Apache-2.0`
                            [Reflections](https://github.com/ronmamo/reflections): `WTFPL`
                            """, false)
                    .addField("테스트/로깅 라이브러리", """
                            [JUnit 5](https://junit.org/junit5/): `EPL-2.0`
                            [SLF4J](https://www.slf4j.org/): `MIT`
                            [Logback](https://logback.qos.ch/): `EPL-1.0 & LGPL-2.1`
                            """, false)
                    .setFooter("다이아 덩어리를 굴러가게 만드는 코드 덩어리들")
                    .build()).queue();
        }
    }
}
