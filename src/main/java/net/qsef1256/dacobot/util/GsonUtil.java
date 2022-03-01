package net.qsef1256.dacobot.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GsonUtil {

    @Getter
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static String parsePretty(String inputString) {
        final JsonObject inputJson = gson.fromJson(inputString, JsonObject.class);

        return gson.toJson(inputJson);
    }

}
