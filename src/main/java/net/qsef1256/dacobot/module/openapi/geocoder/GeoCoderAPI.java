package net.qsef1256.dacobot.module.openapi.geocoder;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.module.openapi.APIConnector;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dialib.util.gson.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@SpringBootApplication
public class GeoCoderAPI implements CommandLineRunner {

    private final DiaSetting setting;

    @Autowired
    public GeoCoderAPI(DiaSetting setting) {
        this.setting = setting;
    }

    public static void main(String[] args) {
        SpringApplication.run(DacoBot.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String address = "동편로 80";
        String token = setting.getKey().getString("geocoder.token");
        String urlString = "http://api.vworld.kr/req/address" +
                "?" + URLEncoder.encode("service", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("address", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("request", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("getcoord", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("version", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("2.0", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("crs", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("epsg:4326", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("address", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode(address, StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("refine", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("true", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("simple", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("false", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("format", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("json", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("type", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode("road", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("key", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        try (APIConnector connector = new APIConnector()) {
            StringBuilder sb = connector.getResultAsString(urlString);
            log.info(GsonUtil.parsePretty(sb.toString()));
        }
    }

}
