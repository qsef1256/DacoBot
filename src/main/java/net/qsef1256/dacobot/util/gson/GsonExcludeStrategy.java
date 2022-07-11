package net.qsef1256.dacobot.util.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import org.jetbrains.annotations.NotNull;

public class GsonExcludeStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(@NotNull FieldAttributes f) {
        GsonExclude annotation = f.getAnnotation(GsonExclude.class);
        return annotation != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

}