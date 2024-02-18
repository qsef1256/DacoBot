package net.qsef1256.dacobot.module.openapi.weather.command;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.qsef1256.dacobot.core.command.DacoCommand;
import net.qsef1256.dacobot.module.openapi.weather.ShortWeatherAPI;
import net.qsef1256.dacobot.module.openapi.weather.enums.WeatherCode;
import net.qsef1256.dacobot.module.openapi.weather.model.Forecast;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class WeatherCommand extends DacoCommand {

    @Setter(onMethod_ = {@Autowired})
    private ShortWeatherAPI weatherAPI;

    public WeatherCommand() {
        name = "날씨";
        help = "오늘 날씨를 확인합니다. (초단기 실황)";
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        event.deferReply().queue(message -> {
            Forecast forecast;

            try {
                forecast = weatherAPI.getForecast(60, 123);
            } catch (IOException | RuntimeException e) {
                message.editOriginalEmbeds(DiaEmbed.error("날씨 불러오기 실패",
                        "기상청이 일을 안하는 것 같네요...",
                        null,
                        null).build()).queue();
                log.warn(e.getMessage());
                return;
            }

            EmbedBuilder embedBuilder = DiaEmbed.info("현재 날씨", null, null);
            embedBuilder.setDescription(LocalDateTimeUtil.getTimeString(forecast.getDateTime()) + " 기준");
            addWeatherInfo(forecast, embedBuilder);
            embedBuilder.setFooter("provided by 기상청");

            message.editOriginalEmbeds(embedBuilder.build()).queue();
        });
    }

    private static void addWeatherInfo(Forecast weather, EmbedBuilder embedBuilder) {
        List<WeatherCode> codeList = List.of(
                WeatherCode.RAIN_TYPE, WeatherCode.RAIN_HOUR, WeatherCode.HUMIDITY,
                WeatherCode.WIND_DIRECT, WeatherCode.WIND_SPEED, WeatherCode.TEMP);

        codeList.forEach(code -> {
            if (code == WeatherCode.EW_WIND_SPEED || code == WeatherCode.NS_WIND_SPEED) return;

            embedBuilder.addField(code.getEmoji() + code.getDesc(), code.getDisplay(weather.getValue(code)), true);
        });
    }

}
