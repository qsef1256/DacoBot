package net.qsef1256.dacobot.module.openapi.weather;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.module.openapi.weather.model.Forecast;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@Slf4j
@SpringBootTest
class ShortWeatherAPITest {

    @Test
    void run(@NotNull ShortWeatherAPI api) throws IOException {
        Forecast weather = api.getWeather(60, 123);

        weather.forEach((code, value) -> log.info("%s%s %s".formatted(
                code.getEmoji(),
                code.getDesc(),
                code.getDisplay(value))));
    }

}