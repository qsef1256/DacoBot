package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.core.localization.TimeLocalizer;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import net.qsef1256.dialib.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Component
public class CreditCommand extends SlashCommand {

    public CreditCommand(@NotNull MainInfoCommand mainInfoCommand,
                         @NotNull LibraryCommand libraryCommand,
                         @NotNull ApiCommand apiCommand) {
        name = "정보";
        help = "다이아 덩어리의 구성 성분";

        children = new SlashCommand[]{
                mainInfoCommand,
                libraryCommand,
                apiCommand
        };
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        SlashCommand[] children = getChildren();

        event.reply(DiaMessage.needSubCommand(children, event.getMember())).queue();
    }

    @Component
    public static class MainInfoCommand extends SlashCommand {

        private final JdaService jdaService;
        private final BuildProperties buildProperties;
        private final DiaSetting diaSetting;

        public MainInfoCommand(@NotNull JdaService jdaService,
                               @NotNull DiaSetting diaSetting,
                               @NotNull BuildProperties buildProperties) {
            this.jdaService = jdaService;
            this.diaSetting = diaSetting;
            this.buildProperties = buildProperties;

            name = "보기";
            help = "전반적인 정보를 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            try {
                long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
                String message = RandomUtil.getRandomElement(
                        Arrays.asList("폭발은 예술이다!",
                                "흠...",
                                "연락처는 장식이다 카더라",
                                "(할말 없음)",
                                "멘트 추천은 본체한테 DM",
                                "나는 댕청하다, /댕청"));

                String formattedUptime = TimeLocalizer.format(Duration.ofMillis(uptime));
                String name = buildProperties.getName();
                String version = buildProperties.getVersion();

                int serverSize = jdaService.getGuilds().size();
                int userSize = jdaService.getUsers().size();

                String jdaVersion = diaSetting.getProject().getString("jda.version");
                String springBootVersion = diaSetting.getProject().getString("spring.boot.version");

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
                        .setFooter("provided by Spring Boot v%s, JDA v%s".formatted(springBootVersion, jdaVersion))
                        .build()).queue();
            } catch (final RuntimeException e) {
                log.error("failed to get bot information", e);

                event.replyEmbeds(DiaEmbed.error("정보 확인 실패",
                        "봇 정보 확인에 실패했습니다.",
                        null,
                        null).build()).queue();
            }
        }

    }

    @Component
    public static class LibraryCommand extends SlashCommand {

        public LibraryCommand() {
            name = "라이브러리";
            help = "사용 중인 라이브러리 & 라이센스 목록을 확인합니다.";
        }

        // TODO: add Spring
        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            event.replyEmbeds(DiaEmbed.primary("라이브러리",
                            "다이아 덩어리를 굴러가게 만드는 코드 덩어리들\n\n저작자 표기는 README.md 또는 해당 웹사이트를 참고하세요.",
                            null)
                    .addField("코어 라이브러리", """
                            [JDA](https://github.com/DV8FromTheWorld/JDA): `Apache-2.0`
                            [Chewtils](https://github.com/Chew/JDA-Chewtils): `Apache-2.0`
                            """, false)
                    .addField("DB 라이브러리", """
                            [Hibernate ORM](https://hibernate.org/orm/): `LGPL-2.1`
                            [Hibernate Validator](https://hibernate.org/validator/): `Apache-2.0`
                            [HikariCP](https://github.com/brettwooldridge/HikariCP): `Apache-2.0`
                            [MariaDB Connector/J](https://mariadb.com/kb/en/mariadb-connector-j/): `LGPL-2.1-or-later`
                            [Spring Data JPA](https://spring.io/projects/spring-data-jpa): `Apache-2.0`
                            """, false)
                    .addField("기술 라이브러리", """
                            [Jandex]("https://github.com/smallrye/jandex): `Apache-2.0`
                            [Jakarta EL Implementation](https://projects.eclipse.org/projects/ee4j.el): `EPL-2.0` `GPL-2.0-with-classpath-exception`
                            """, false)
                    .addField("기능 라이브러리", """
                            [mXParser](https://mathparser.org/): `BSD-2-Clause`
                            """, false)
                    .addField("유틸 라이브러리", """
                            [Lombok](https://projectlombok.org/): `MIT`
                            [Guava](https://github.com/google/guava): `Apache-2.0`
                            [Caffeine](https://github.com/ben-manes/caffeine): `Apache-2.0`
                            [Gson](https://github.com/google/gson): `Apache-2.0`
                            [SnakeYAML](https://bitbucket.org/snakeyaml/snakeyaml/src/master/): `Apache-2.0`
                            [dom4j](https://dom4j.github.io/): `Plexus`
                            [Reflections](https://github.com/ronmamo/reflections): `WTFPL`
                            """, false)
                    .addField("Apache 라이브러리", """
                            [Apache Commons Lang3](https://github.com/apache/commons-lang): `Apache-2.0`
                            [Apache Commons DBCP](https://commons.apache.org/proper/commons-dbcp/): `Apache-2.0`
                            [Apache Commons Configuration](https://commons.apache.org/proper/commons-configuration/): `Apache-2.0`
                            [Apache Commons BeanUtils](https://commons.apache.org/proper/commons-beanutils/): `Apache-2.0`
                            [Apache Commons IO](https://commons.apache.org/proper/commons-io/): `Apache-2.0`
                            [Apache Maven Model](https://maven.apache.org/ref/3.8.6/maven-model/): `Apache-2.0`
                            """, false)
                    .addField("테스트/로깅 라이브러리", """
                            [JUnit 5](https://junit.org/junit5/): `EPL-2.0`
                            [SLF4J](https://www.slf4j.org/): `MIT`
                            [Logback](https://logback.qos.ch/): `EPL-1.0` `LGPL-2.1`
                            """, false)
                    .addField("기타 라이센스", """
                            [Twemoji](https://twemoji.twitter.com/): `MIT`, `CC-BY-4.0`
                            """, false)
                    .setFooter("SPDX (https://spdx.org/licenses/) 의 표기를 따릅니다.")
                    .build()).queue();
        }

    }

    @Component
    public static class ApiCommand extends SlashCommand {

        public ApiCommand() {
            name = "api";
            help = "사용중인 API 정보를 확인합니다.";
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            event.replyEmbeds(DiaEmbed.primary("외부 API", "다이아 덩어리를 굴러가게 만드는 서비스들", null)
                    .addField("기상청 단기예보 조회 서비스",
                            "[Link](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15084084) 제공 `기상청`", false)
                    .addField("기상청 중기예보 조회 서비스",
                            "[Link](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15059468) 제공 `기상청`", false)
                    .build()).queue();
        }

    }

}
