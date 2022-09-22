package nc.apps.lab3.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class HttpUtil {

    public static String get(String url, String params) {
       //return get(url, params, Charset.forName("utf-8"));
        return getViaRestTemplate(url,params);
    }

    public static String get(String url, String params, Charset charset) {
        String result = "";

        if (null != params && !params.equals("")) {
            if (url.contains("?")) {
                url += "&" + params;
            } else {
                url += "?" + params;
            }
        }
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
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


    private static String getViaRestTemplate(String url, String params) {
        String result = "";

        RestTemplate restTemplate = new RestTemplate();
        if (null != params && !params.equals("")) {
            if (url.contains("?")) {
                url += "&" + params;
            } else {
                url += "?" + params;
            }
        }
        try{
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        result = "{\"responsecode\": "+response.getStatusCodeValue()+","+
                "\"message\": "+"\""+response.getStatusCode().getReasonPhrase()+"\","+
                "\"data\": "+response.getBody()+"}";
        } catch (HttpClientErrorException e) {
            result = "{\"responsecode\": "+e.getStatusCode().value()+","+
                    "\"message\": "+"\""+e.getStatusCode().getReasonPhrase()+"\","+
                    "\"data\": "+e.getMessage().replace(e.getStatusCode().value()+" "+e.getStatusCode().getReasonPhrase()+":","").trim().replace("\"{","{").replace("}\"","}")+"}";
        }
        return result;
    }
}