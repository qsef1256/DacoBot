package net.qsef1256.dacobot.setting;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.util.CommonUtil;
import net.qsef1256.dacobot.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.ZoneId;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import static net.qsef1256.dacobot.DacoBot.logger;

@UtilityClass
public class DiaSetting {

    public static final String SETTING_NAME = "setting";
    public static final String KEY_NAME = "key";

    @Getter
    private static Properties setting;
    @Getter
    private static Properties key;
    @Getter
    private static ZoneId zoneId;

    static {
        try {
            DiaSetting.initSettings();
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void initSettings() throws InvalidPropertiesFormatException {
        try {
            setting = PropertiesUtil.loadFile(SETTING_NAME);
            key = PropertiesUtil.loadFile(KEY_NAME);
        } catch (final RuntimeException e) {
            logger.error("Error on loading properties");
            e.printStackTrace();
        }

        if (CommonUtil.anyNull(setting, key))
            throw new InvalidPropertiesFormatException("bot properties is null");

        zoneId = ZoneId.of(getSetting().getProperty("bot.timer.zone"));

        logger.info("main Package: %s".formatted(setting.getProperty("main.package")));
    }

    public static Guild getGuild() {
        return DacoBot.getJda().getGuildById(getGuildID());
    }

    public static @NotNull Long getGuildID() {
        return Long.parseLong(DiaSetting.getSetting().getProperty("bot.guildId"));
    }

    public static MessageChannel getMainChannel() {
        return getGuild().getTextChannelById(getMainChannelId());
    }

    public static @NotNull Long getMainChannelId() {
        return Long.parseLong(DiaSetting.getSetting().getProperty("bot.mainChannelId"));
    }

    // TODO: for external resource (like config file)
    // TODO: 예를 들어 단독 jar 파일로 실행되는 경우 config.properties 파일을 resource 에서 찾는 것은 비효율적
    @NotNull
    public static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        return path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    }

}
