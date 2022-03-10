package net.qsef1256.dacobot.system.openapi.geocoder;

import net.qsef1256.dacobot.setting.DiaSetting;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static net.qsef1256.dacobot.system.openapi.APIConnector.getResult;
import static net.qsef1256.dacobot.util.GsonUtil.parsePretty;

public class GeoCoder {

    public static void main(String[] args) throws IOException {

        String address = "동편로 80";
        String token = DiaSetting.getKey().getProperty("geocoder.token");
        String urlString = "http://api.vworld.kr/req/address" +
                "?" + URLEncoder.encode("service", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("address", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("request", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("getcoord", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("version", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("2.0", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("crs", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("epsg:4326", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("address", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(address, StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("refine", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("true", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("simple", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("false", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("format", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("json", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("type", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("road", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("key", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        StringBuilder sb = getResult(urlString);
        System.out.println(parsePretty(sb.toString()));
    }

}
