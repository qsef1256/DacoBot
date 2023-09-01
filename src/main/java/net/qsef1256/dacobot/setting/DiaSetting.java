package net.qsef1256.dacobot.setting;

import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dialib.io.FileLoader;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DiaSetting {

    @Getter
    private static final DiaSetting instance = new DiaSetting();

    private Configuration setting;
    private Configuration project;
    private Configuration key;
    private ZoneId zoneId;

    @SneakyThrows
    private DiaSetting() {
        init();
    }

    public void init() throws ConfigurationException {
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
        return FileLoader.get(fileName, this.getClass());
    }

    @NotNull
    private File getProjectFile() throws IOException {
        return FileLoader.getFromResource("project.properties", getClass().getClassLoader());
    }

    public Guild getMainGuild() {
        return DacoBot.getJda().getGuildById(getMainGuildID());
    }

    public @NotNull Long getMainGuildID() {
        return Long.parseLong(getSetting().getString("bot.mainGuildId"));
    }

    public MessageChannel getMainChannel() {
        return getMainGuild().getTextChannelById(getMainChannelId());
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

    public @NotNull List<Guild> getAllGuilds() {
        List<Guild> guilds = new ArrayList<>();

        guilds.add(getMainGuild());
        for (String subGuildId : getSetting().getString("bot.subGuildIds").split(",\\s*")) {
            guilds.add(DacoBot.getJda().getGuildById(subGuildId));
        }

        return guilds;
    }

}
