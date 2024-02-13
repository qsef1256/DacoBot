package net.qsef1256.dacobot.core.boot;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DacoBootConfig {

    private final DacoBootstrapper bootstrapper;

    public DacoBootConfig(@NotNull DacoBootstrapper bootstrapper) {
        this.bootstrapper = bootstrapper;
    }

    @ConditionalOnProperty(value = "dacobot.jda.boot",
            havingValue = "true",
            matchIfMissing = true)
    public void boot() throws Exception {
        bootstrapper.boot();
    }

}
