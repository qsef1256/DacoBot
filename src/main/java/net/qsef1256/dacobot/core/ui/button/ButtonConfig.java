package net.qsef1256.dacobot.core.ui.button;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ButtonConfig {

    @Bean(name = "botButtons")
    public Map<String, DacoButton> getButtons(@NotNull List<? extends DacoButton> buttons) {
        return buttons.stream().collect(Collectors.toMap(
                Button::getId,
                Function.identity()));
    }

}
