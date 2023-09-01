package net.qsef1256.dacobot.module.openapi.weather.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.qsef1256.dacobot.module.openapi.weather.enums.WeatherCode;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

public class Forecast {

    @Getter
    private final Map<WeatherCode, Double> forecastMap = new EnumMap<>(WeatherCode.class);

    @Getter
    private LocalDateTime dateTime;

    /**
     * Construct WeatherContainer.
     *
     * @param jsonObject Input Json
     * @throws NoSuchElementException category is illegal (parse failed)
     */
    public Forecast(JsonObject jsonObject) {
        parseForecast(jsonObject);
    }

    private void parseForecast(@NotNull JsonObject response) {
        JsonArray items = response.get("body").getAsJsonObject().get("items").getAsJsonObject().get("item").getAsJsonArray();

        JsonObject firstJson = items.get(0).getAsJsonObject();
        String date = firstJson.get("baseDate").getAsString();
        String time = firstJson.get("baseTime").getAsString();

        dateTime = LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("yyyyMMdd HHmm"));

        items.forEach(item -> {
            String category = item.getAsJsonObject().get("category").getAsString();
            double value = item.getAsJsonObject().get("obsrValue").getAsDouble();
            WeatherCode code = WeatherCode.findByCode(category);

            if (code == null) throw new NoSuchElementException("Can't find " + category + " value category");

            putWeather(code, value);
        });
    }

    public boolean isWeatherExist(WeatherCode code) {
        return forecastMap.containsKey(code);
    }

    public Double getValue(WeatherCode code) {
        return forecastMap.get(code);
    }

    public void putWeather(WeatherCode code, double value) {
        forecastMap.put(code, value);
    }

    public void forEach(BiConsumer<? super WeatherCode, ? super Double> action) {
        forecastMap.forEach(action);
    }

}
