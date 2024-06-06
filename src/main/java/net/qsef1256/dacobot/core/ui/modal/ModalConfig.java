package net.qsef1256.dacobot.core.ui.modal;

import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ModalConfig {

    @Bean(name = "botModals")
    public Map<String, DacoModal> getModals(@NotNull List<? extends DacoModal> modals) {
        return modals.stream().collect(Collectors.toMap(
                Modal::getId,
                Function.identity()));
    }

}
