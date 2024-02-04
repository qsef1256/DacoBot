package net.qsef1256.dacobot.module.openapi.weather;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.module.openapi.APIConnector;
import net.qsef1256.dacobot.module.openapi.enums.APICode;
import net.qsef1256.dacobot.module.openapi.weather.model.Forecast;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dialib.util.gson.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.SyncFailedException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ShortWeatherAPI {

    @Setter(onMethod_ = {@Autowired})
    private DiaSetting setting;

    /**
     * 기상청으로부터 데이터를 받습니다.
     *
     * @param x x (예보 지점)
     * @param y y (예보 지점)
     * @return forecast data
     * @throws IOException                      can't connect to api server
     * @throws IllegalArgumentException         Failed retrieve normal data
     * @throws java.util.NoSuchElementException parse failed
     */
    public Forecast getForecast(int x, int y) throws IOException {
        LocalDateTime toFind = getDateTime();

        String date = toFind.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = toFind.format(DateTimeFormatter.ofPattern("HHmm"));

        String urlString = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst" +
                "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) +
                "=" + setting.getKey().getString("weather.token") + /* Service Key */
                "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /* 페이지 번호 */
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("10", StandardCharsets.UTF_8) + /* 한 페이지 결과 수 */
                "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) + /* 요청 자료 형식(XML/JSON) */
                "&" + URLEncoder.encode("base_date", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode(date, StandardCharsets.UTF_8) + /* ‘21년 6월 28일 발표 */
                "&" + URLEncoder.encode("base_time", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode(time, StandardCharsets.UTF_8) + /* 06시 발표(정시 단위) */
                "&" + URLEncoder.encode("nx", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode(String.valueOf(x), StandardCharsets.UTF_8) + /* 예보 지점의 X 좌표값 */
                "&" + URLEncoder.encode("ny", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode(String.valueOf(y), StandardCharsets.UTF_8); /* 예보 지점의 Y 좌표값 */
        StringBuilder sb;
        Gson gson = GsonUtil.getGson();
        JsonObject jsonObject;

        try (APIConnector connector = new APIConnector()) {
            sb = connector.getResultAsString(urlString);

            jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
        }

        if (jsonObject == null) throw new SyncFailedException("기상청에서 받은 데이터를 Json 으로 변환하는데 실패했습니다.");

        JsonObject response = jsonObject.getAsJsonObject()
                .get("response")
                .getAsJsonObject();

        String resultCode = response.get("header")
                .getAsJsonObject()
                .get("resultCode")
                .getAsString();

        APICode apiCode = APICode.findByCode(resultCode);
        if (apiCode != APICode.NORMAL_SERVICE)
            throw new IllegalArgumentException("기상청 API 와 연동하는데 실패했습니다: " + (apiCode != null ? apiCode.getDisplay() : null));

        return new Forecast(response);
    }

    private LocalDateTime getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return (now.getMinute() < 45) ? now.minusHours(1) : now; // 정시 45분 마다 업데이트
    }

}
