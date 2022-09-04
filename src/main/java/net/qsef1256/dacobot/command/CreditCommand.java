package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.localization.TimeLocalizer;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import net.qsef1256.dacobot.util.CommonUtil;
import net.qsef1256.dacobot.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class CreditCommand extends SlashCommand {

    public CreditCommand() {
        name = "정보";
        help = "다이아 덩어리의 구성 성분";

        children = new SlashCommand[]{
                new MainInfoCommand(),
                new LibraryCommand(),
                new APICommand()
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
                properties = PropertiesUtil.loadFile("project");
            } catch (final RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error("정보 확인 실패", "봇 정보 확인에 실패했습니다.", null, null).build()).queue();
                e.printStackTrace();
            }

            if (properties == null) return;
            final long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
            final String message = CommonUtil.getRandomElement(
                    Arrays.asList("폭발은 예술이다!", "흠...", "연락처는 장식이다 카더라", "(할말 없음)", "멘트 추천은 본체한테 DM", "나는 댕청하다, /댕청"));

            final String formattedUptime = TimeLocalizer.format(Duration.ofMillis(uptime));
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
            event.replyEmbeds(DiaEmbed.main("라이브러리", "다이아 덩어리를 굴러가게 만드는 코드 덩어리들\n\n저작자 표기는 README.md 또는 해당 웹사이트를 참고하세요.", null)
                    .addField("코어 라이브러리", """
                            [JDA](https://github.com/DV8FromTheWorld/JDA): `Apache-2.0`
                            [Chewtils](https://github.com/Chew/JDA-Chewtils): `Apache-2.0`
                            """, false)
                    .addField("DB 라이브러리", """
                            [Hibernate ORM](https://hibernate.org/orm/): `LGPL-2.1`
                            [HikariCP](https://github.com/brettwooldridge/HikariCP): `Apache-2.0`
                            [MariaDB Connector/J](https://mariadb.com/kb/en/mariadb-connector-j/): `LGPL-2.1-or-later`
                            [Spring Data JPA](https://spring.io/projects/spring-data-jpa): `Apache-2.0`
                            """, false)
                    .addField("기술 라이브러리", """
                            [Guice](https://github.com/google/guice): `Apache-2.0`
                            [AspectJ](https://projects.eclipse.org/projects/tools.aspectj): `EPL-1.0`
                            """, false)
                    .addField("기능 라이브러리", """
                            [mXParser](https://mathparser.org/): `BSD-2-Clause`
                            [Program AB](https://code.google.com/archive/p/program-ab/): `LGPL-2.0-or-later`
                            """, false)
                    .addField("유틸 라이브러리", """
                            [Lombok](https://projectlombok.org/): `MIT`
                            [Guava](https://github.com/google/guava): `Apache-2.0`
                            [Gson](https://github.com/google/gson): `Apache-2.0`
                            [SnakeYAML](https://bitbucket.org/snakeyaml/snakeyaml/src/master/): `Apache-2.0`
                            [dom4j](https://dom4j.github.io/): `Plexus`
                            [Reflections](https://github.com/ronmamo/reflections): `WTFPL`
                            """, false)
                    .addField("Apache 라이브러리", """
                            [Apache Commons Lang3](https://github.com/apache/commons-lang): `Apache-2.0`
                            [Apache Commons DBCP](https://commons.apache.org/proper/commons-dbcp/):`Apache-2.0`
                            [Apache Commons Configuration](https://commons.apache.org/proper/commons-configuration/):`Apache-2.0`
                            [Apache Commons BeanUtils](https://commons.apache.org/proper/commons-beanutils/):`Apache-2.0`
                            """, false)
                    .addField("테스트/로깅 라이브러리", """
                            [JUnit 5](https://junit.org/junit5/): `EPL-2.0`
                            [SLF4J](https://www.slf4j.org/): `MIT`
                            [Logback](https://logback.qos.ch/): `EPL-1.0 AND LGPL-2.1`
                            """, false)
                    .addField("기타 라이센스", """
                            [Twemoji](https://twemoji.twitter.com/): `MIT`, `CC-BY-4.0`
                            """, false)
                    .setFooter("SPDX (https://spdx.org/licenses/) 의 표기를 따릅니다.")
                    .build()).queue();
        }
    }

    private static class APICommand extends SlashCommand {

        public APICommand() {
            name = "api";
            help = "사용중인 API 정보를 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            event.replyEmbeds(DiaEmbed.main("외부 API", "다이아 덩어리를 굴러가게 만드는 서비스들", null)
                    .addField("기상청 단기예보 조회 서비스",
                            "[Link](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15084084) 제공 `기상청`", false)
                    .addField("기상청 중기예보 조회 서비스",
                            "[Link](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15059468) 제공 `기상청`", false)
                    .addField("보건복지부 코로나19 감염현황 조회 서비스",
                            "[Link](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15043376) 제공 `보건복지부`", false)
                    .addField("Geocoder API 2.0",
                            "[Link](https://www.vworld.kr/dev/v4dv_geocoderguide2_s001.do) 제공 `국토교통부`", false)
                    .build()).queue();
        }
    }
}
