package ok.apps.newsapi.util;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.stream.Collectors;

@Component
public class HttpUtil {

    @Cacheable(value = "news", key = "#cacheString")
    public String get(String url, String params, String cacheString) {
        return get(url, params, Charset.forName("utf-8"));
        //return getViaRestTemplate(url,params);
    }

    public String get(String url, String params, Charset charset) {
        String result = "";

        if (null != params && !params.equals("")) {
            if (url.contains("?")) {
                url += "&" + params;
            } else {
                url += "?" + params;
            }
        }

        TrustManager[] trustAllCerts = getTrustManager();

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        HttpsURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpsURLConnection) realUrl.openConnection();
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
        if (responseCode == 200) {
            try (InputStream in = conn.getInputStream();
                 BufferedReader bufIn = new BufferedReader(new InputStreamReader(in, charset))) {
                result = "{\"responsecode\": " + responseCode + "," +
                        "\"message\": " + "\"" + message + "\"," +
                        "\"data\": " + bufIn.lines().collect(Collectors.joining("\n")) + "}";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try (InputStream in = conn.getErrorStream();
                 BufferedReader bufIn = new BufferedReader(new InputStreamReader(in, charset))) {
                result = "{\"responsecode\": " + responseCode + "," +
                        "\"message\": " + "\"" + message + "\"," +
                        "\"data\": " + bufIn.lines().collect(Collectors.joining("\n")) + "}";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }


    private String getViaRestTemplate(String url, String params) {
        String result = "";

        RestTemplate restTemplate = new RestTemplate();
        if (null != params && !params.equals("")) {
            if (url.contains("?")) {
                url += "&" + params;
            } else {
                url += "?" + params;
            }
        }
        try {
            ResponseEntity<String> response
                    = restTemplate.getForEntity(url, String.class);

            result = "{\"responsecode\": " + response.getStatusCodeValue() + "," +
                    "\"message\": " + "\"" + response.getStatusCode().getReasonPhrase() + "\"," +
                    "\"data\": " + response.getBody() + "}";
        } catch (HttpClientErrorException e) {
            result = "{\"responsecode\": " + e.getStatusCode().value() + "," +
                    "\"message\": " + "\"" + e.getStatusCode().getReasonPhrase() + "\"," +
                    "\"data\": " + e.getMessage().replace(e.getStatusCode().value() + " " + e.getStatusCode().getReasonPhrase() + ":", "").trim().replace("\"{", "{").replace("}\"", "}") + "}";
        }
        return result;
    }

    public String saveImage(String imagePath, Path path, String id) throws Exception {
        TrustManager[] trustAllCerts = getTrustManager();

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        URLConnection openConnection = new URL(imagePath).openConnection();
        openConnection.addRequestProperty("accept", "*/*");
        openConnection.addRequestProperty("connection", "Keep-Alive");
        openConnection.addRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        BufferedImage image = ImageIO.read(openConnection.getInputStream());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        File file = new File(path.toString(), "article_"+id+".png");
        if (file.exists()) {
            file.delete();
        }
        ImageIO.write(image, "png", file);
        return file.getName();
    }

    public TrustManager[] getTrustManager() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
    }
}