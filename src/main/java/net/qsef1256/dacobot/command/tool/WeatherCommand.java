package net.qsef1256.dacobot.command.tool;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.qsef1256.dacobot.service.notification.DiaEmbed;
import net.qsef1256.dacobot.service.openapi.weather.ShortWeatherAPI;
import net.qsef1256.dacobot.service.openapi.weather.enums.WeatherCode;
import net.qsef1256.dacobot.service.openapi.weather.model.Forecast;
import net.qsef1256.dacobot.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static net.qsef1256.dacobot.DacoBot.logger;

public class WeatherCommand extends SlashCommand {

    public WeatherCommand() {
        name = "날씨";
        help = "오늘 날씨를 확인합니다. (초단기 실황)";
    }

    @Override
    protected void execute(@NotNull SlashCommandEvent event) {
        event.deferReply().queue(message -> {
            Forecast weather;

            try {
                weather = ShortWeatherAPI.getWeather(60, 123);
            } catch (IOException | RuntimeException e) {
                message.editOriginalEmbeds(DiaEmbed.error("날씨 불러오기 실패", "기상청이 일을 안하는 것 같네요...", null, null).build()).queue();
                logger.warn(e.getMessage());
                return;
            }

            EmbedBuilder embedBuilder = DiaEmbed.info("현재 날씨", null, null);
            embedBuilder.setDescription(LocalDateTimeUtil.getTimeString(weather.getDateTime()) + " 기준");

            List<WeatherCode> codeList = List.of(
                    WeatherCode.RAIN_TYPE, WeatherCode.RAIN_HOUR, WeatherCode.HUMIDITY,
                    WeatherCode.WIND_DIRECT, WeatherCode.WIND_SPEED, WeatherCode.TEMP);

            codeList.forEach(code -> {
                if (code == WeatherCode.EW_WIND_SPEED || code == WeatherCode.NS_WIND_SPEED) return;

                embedBuilder.addField(code.getEmoji() + code.getDesc(), code.getDisplay(weather.getValue(code)), true);
            });

            embedBuilder.setFooter("provided by 기상청");
            message.editOriginalEmbeds(embedBuilder.build()).queue();
        });
    }
}
