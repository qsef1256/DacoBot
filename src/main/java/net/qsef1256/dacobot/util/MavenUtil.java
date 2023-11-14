package net.qsef1256.dacobot.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

@Slf4j
@UtilityClass
public class MavenUtil {

    @NotNull
    @SneakyThrows
    public static Model getMavenModel(String groupId, String artifactId) {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model;
            if (new File("pom.xml").exists()) {
                model = reader.read(new FileReader("pom.xml"));
            } else {
                String pomPath = "META-INF/maven/%s/%s/pom.xml".formatted(groupId, artifactId);
                model = reader.read(new InputStreamReader(
                        Objects.requireNonNull(MavenUtil.class.getClassLoader().getResourceAsStream(pomPath))));
            }

            if (model == null) throw new FileNotFoundException("pom.xml is null");
            return model;
        } catch (final IOException | XmlPullParserException e) {
            log.error(e.getMessage(), e);

            throw new FileNotFoundException("failed to find maven model");
        }
    }

}
