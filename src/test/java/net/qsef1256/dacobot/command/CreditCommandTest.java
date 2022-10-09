package net.qsef1256.dacobot.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
class CreditCommandTest {

    @Test
    void credit() {
        assertDoesNotThrow(() -> {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model;

            try {
                model = reader.read(new FileReader("pom.xml"));

                log.info("Name: " + model.getName());
                log.info("Author: " + model.getDevelopers().stream()
                        .map(Contributor::getName)
                        .collect(Collectors.joining(", ")));
            } catch (XmlPullParserException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
