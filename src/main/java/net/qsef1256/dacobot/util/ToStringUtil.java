package net.qsef1256.dacobot.util;

import com.google.common.base.Joiner;
import lombok.experimental.UtilityClass;
import net.qsef1256.dacobot.service.key.ManagedKey;
import net.qsef1256.dacobot.service.message.data.MessageData;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

// lombok @ToString is not enough :(
@UtilityClass
public class ToStringUtil {

    public static @NotNull String mapToString(Map<ManagedKey, MessageData> map) {
        return Joiner.on("|").withKeyValueSeparator(":").join(map);
    }

}
