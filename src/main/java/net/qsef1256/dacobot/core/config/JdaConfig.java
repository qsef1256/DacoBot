package net.qsef1256.dacobot.core.config;

import com.jagrosh.jdautilities.command.CommandClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dacobot.util.ReflectionUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class JdaConfig {

    private final List<? extends ListenerAdapter> listeners;

    public JdaConfig(List<? extends ListenerAdapter> listeners) {
        this.listeners = listeners;
    }

    @Bean
    public JDA getJda(@NotNull DiaSetting setting,
                      @NotNull CommandClient commandClient) {
        JDABuilder builder = JDABuilder.createDefault(setting
                .getKey()
                .getString("discord.token"));

        configureBot(builder);
        builder.addEventListeners(commandClient);
        registerListeners(builder);

        return builder.build();
    }

    private void configureBot(@NotNull JDABuilder builder) {
        builder.disableCache(CacheFlag.VOICE_STATE);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL); // 모든 길드의 유저 캐싱하기
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT);
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
    }

    @SneakyThrows
    private void registerListeners(@NotNull JDABuilder builder) {
        log.info("Loading Listeners");

        if (listeners.isEmpty())
            log.warn("There is no listener in the registered package. No listeners were loaded.");
        for (ListenerAdapter listener : listeners) {
            if (!ReflectionUtil.isConcrete(listener.getClass())) continue;

            builder.addEventListeners(listener);
            log.info("Loaded %s successfully".formatted(listener.getClass().getSimpleName()));
        }
    }

}
