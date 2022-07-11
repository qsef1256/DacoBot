package net.qsef1256.dacobot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class URLUtil {

    /**
     * 입력된 문자열 끝의 / 를 없앱니다.
     *
     * @param url input URL or String
     * @return trimmed URL
     */
    public String trimEndSlash(@NotNull String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

}
