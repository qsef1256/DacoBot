package net.qsef1256.dacobot.system.openapi;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIConnector {

    /**
     * URL 로부터 데이터를 Json 으로 얻어옵니다.
     *
     * <p><b>주의: </b>비동기로 처리 되어야 합니다.</p>
     * @param urlString api url
     * @return result StringBuilder
     * @throws IOException when can't connect to api url
     */
    @NotNull
    public static StringBuilder getResult(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        int code = conn.getResponseCode();

        BufferedReader br = (code >= 200 && code <= 300) ?
                new BufferedReader(new InputStreamReader(conn.getInputStream())) :
                new BufferedReader(new InputStreamReader(conn.getErrorStream()));

        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        conn.disconnect();
        return sb;
    }

}
