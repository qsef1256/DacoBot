package net.qsef1256.dacobot.setting;

import lombok.Getter;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.util.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class DiaSetting {

    @Getter
    private static Reflections reflections;
    @Getter
    private static Properties setting;
    @Getter
    private static Properties key;

    static {
        try {
            DiaSetting.initSettings();
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void initSettings() throws InvalidPropertiesFormatException {
        try {
            setting = getProperties("setting.properties");
            key = getProperties("key.properties");
        } catch (final IOException | RuntimeException e) {
            DacoBot.logger.error("Error on loading properties");
            e.printStackTrace();
        }

        if (CommonUtil.anyNull(setting, key))
            throw new InvalidPropertiesFormatException("bot properties is null");

        String mainPackage = setting.getProperty("main.package");
        DacoBot.logger.info("main Package: " + mainPackage);
        reflections = new Reflections(mainPackage);
    }

    @NotNull
    public static Properties getProperties(String path) throws IOException {
        final Properties properties = new Properties();
        properties.load(DacoBot.class.getClassLoader().getResourceAsStream(path));

        return properties;
    }

    @NotNull
    public static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        return path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    }

}
