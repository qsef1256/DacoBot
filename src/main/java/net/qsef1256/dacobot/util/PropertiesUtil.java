package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {

    private static final ClassLoader classLoader = PropertiesUtil.class.getClassLoader();

    /**
     * Properties 파일을 로드합니다. <b>파일 이름에 확장자는 제외합니다.</b>
     *
     * @param fileName File name (ex. key,config)
     * @return properties
     */
    @NotNull
    public static Properties loadFile(final String fileName) {
        String first = "src/main/resources/" + fileName + ".properties";

        if (classLoader.getResource(first) != null)
            return load(first);
        else
            return load(fileName + ".properties");
    }

    @NotNull
    public static Properties load(final String path) {
        final Properties properties = new Properties();
        try {
            properties.load(classLoader.getResourceAsStream(path));
        } catch (final IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Can't find properties in " + path);
        }
        return properties;
    }

}
