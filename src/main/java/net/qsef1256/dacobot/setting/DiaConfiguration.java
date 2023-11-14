package net.qsef1256.dacobot.setting;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiaConfiguration {

    @Bean
    @Autowired
    public Reflections reflections(@NotNull DiaSetting setting) {
        return new Reflections(setting.getMainPackage());
    }

}
