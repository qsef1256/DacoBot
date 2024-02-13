package net.qsef1256.dacobot.setting;

import lombok.Getter;
import lombok.SneakyThrows;
import net.qsef1256.dialib.io.FileLoader;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZoneId;

// TODO: replace to @ConfigurationProperties(prefix = "dacobot")
@Getter
@Component
public class DiaSetting {

    private Configuration setting;
    private Configuration project;
    private Configuration key;
    private ZoneId zoneId;

    public DiaSetting() {
        init();
    }

    @SneakyThrows
    public void init() {
        try {
            setting = new Configurations().properties(getFile("setting.properties"));
            project = new Configurations().properties(getProjectFile());
            key = new Configurations().properties(getFile("key.properties"));

            zoneId = ZoneId.of(getSetting().getString("bot.timer.zone"));
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @NotNull
    @Contract("_ -> new")
    private File getFile(@NotNull String fileName) throws FileNotFoundException {
        return FileLoader.get(fileName, getClass());
    }

    @NotNull
    private File getProjectFile() throws IOException {
        return FileLoader.getFromResource("project.properties", getClass().getClassLoader());
    }

    public @NotNull Long getMainGuildID() {
        return Long.parseLong(getSetting().getString("bot.mainGuildId"));
    }

    public @NotNull Long getMainChannelId() {
        return Long.parseLong(getSetting().getString("bot.mainChannelId"));
    }

    public String getMainPackage() {
        return getSetting().getString("main.package");
    }

    public String getMainClass() {
        return getSetting().getString("main.class");
    }

}
