package nc.apps.lab3.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class HttpUtil {
    /**
     * GET запрос по умолчанию в кодировке UTF-8
     *
     * @param url
     * @param params
     * @return
     */
    public static String get(String url, String params) {
        return get(url, params, Charset.forName("utf-8"));
    }

    /**
     * ПОЛУЧИТЬ запрос
     *
     * @param url Запросить URL
     * @return
     */
    public static String get(String url, String params, Charset charset) {
        String result = "";

        if (null != params && !params.equals("")) {
            if (url.contains("?")) {// содержит ?, добавить & сразу после
                url += "&" + params;
            } else {
                url += "?" + params;
            }
        }
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            // Установить общие атрибуты запроса
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String message = "";
        int responseCode = 0;
        try {
            message = conn.getResponseMessage();
            responseCode = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responseCode==200){
            try (InputStream in = conn.getInputStream();
                 BufferedReader bufIn = new BufferedReader(new InputStreamReader(in,charset))) {
                result = "{\"responsecode\": "+responseCode+","+
                        "\"message\": "+"\""+message+"\","+
                        "\"data\": "+bufIn.lines().collect(Collectors.joining("\n"))+"}";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            try (InputStream in = conn.getErrorStream();
                 BufferedReader bufIn = new BufferedReader(new InputStreamReader(in,charset))) {
                result = "{\"responsecode\": "+responseCode+","+
                        "\"message\": "+"\""+message+"\","+
                        "\"data\": "+bufIn.lines().collect(Collectors.joining("\n"))+"}";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}