package net.qsef1256.dacobot.system.openapi.weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.qsef1256.dacobot.system.openapi.weather.enums.WeatherCode;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

public class WeatherContainer {

    @Getter
    private final Map<WeatherCode, Double> weatherMap = new HashMap<>();

    @Getter
    private LocalDateTime dateTime;

    /**
     * Construct WeatherContainer.
     *
     * @param jsonObject Input Json
     * @throws NoSuchElementException category is illegal (parse failed)
     */
    public WeatherContainer(JsonObject jsonObject) {
        setWeather(jsonObject);
    }

    private void setWeather(@NotNull JsonObject response) {
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
        return weatherMap.containsKey(code);
    }

    public Double getValue(WeatherCode code) {
        return weatherMap.get(code);
    }

    public void putWeather(WeatherCode code, double value) {
        weatherMap.put(code, value);
    }

    public void forEach(BiConsumer<? super WeatherCode, ? super Double> action) {
        weatherMap.forEach(action);
    }

}
