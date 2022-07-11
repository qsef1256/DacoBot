package net.qsef1256.dacobot.util.gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class GsonUtil {

    @Getter
    private static final Gson gson = getGsonBuilder().create();

    @Getter
    private static final Gson gsonNull = getGsonBuilder().serializeNulls().create();

    @NotNull
    private GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTime())
                .registerTypeAdapter(LocalDate.class, new GsonLocalDate())
                .setExclusionStrategies(new GsonExcludeStrategy());
    }

    public String parsePretty(String inputString) {
        final JsonObject inputJson = gson.fromJson(inputString, JsonObject.class);

        return gson.toJson(inputJson);
    }

    public JsonObject toJson(String jsonString) {
        return gson.fromJson(jsonString, JsonObject.class); // TODO: JsonObject.class is safe?
    }

    public String toText(JsonObject jsonObject) {
        return gson.toJson(jsonObject);
    }

}
