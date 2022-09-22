package nc.apps.lab3.util;

import nc.apps.lab3.model.Article;
import nc.apps.lab3.model.DataNewsType;
import nc.apps.lab3.model.Source;
import nc.apps.lab3.model.request.DataRequest;
import nc.apps.lab3.model.request.EverythingRequest;
import nc.apps.lab3.model.request.TopHeadlinesRequest;
import nc.apps.lab3.model.response.ArticleResponse;
import nc.apps.lab3.model.response.DataResponse;
import nc.apps.lab3.model.response.ErrorResponse;
import nc.apps.lab3.model.response.SourceResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONUtil {

    public static DataResponse parseJson(JSONObject jsonObject, DataNewsType type) {
        Iterator<String> iterator = jsonObject.keys();
        int statusCode = 0;
        String statusMessage = "";

        JSONObject jsonData = null;
        DataResponse response = new DataResponse();

        for (; iterator.hasNext(); ) {
            String key = iterator.next();
            Object value;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (key.equals("responsecode") && (value instanceof Integer)) {
                statusCode = (Integer) value;
            }
            if (key.equals("data") && (value instanceof JSONObject)) {
                jsonData = (JSONObject) value;
            }
            if (key.equals("message") && (value instanceof String)) {
                statusMessage = (String) value;
            }
        }

        response.setMessage(statusMessage);
        response.setStatusCode(statusCode);

        if (statusCode == 200){
            response.setData(type == DataNewsType.SOURCE?parseJsonSourceData(jsonData):parseJsonArticleData(jsonData));
         }else{
            response.setData(parseJsonErrorData(jsonData));
        }
        return response;
    }

    private static DataRequest parseJsonRequest(JSONObject jsonRequest) {
        Iterator<String> iterator = jsonRequest.keys();
        DataRequest request = null;

        for (; iterator.hasNext(); ) {
            String key = iterator.next();
            Object value;
            try {
                value = jsonRequest.get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (key.equals("q") && (value instanceof String)) {
                request = new EverythingRequest();
            }
            if (key.equals("sources") && (value instanceof String)) {
                request = new TopHeadlinesRequest();
            }
        }

        return request;
    }

    public static ErrorResponse parseJsonErrorData(JSONObject jsonObject) {
        Iterator<String> iterator = jsonObject.keys();

        ErrorResponse response = new ErrorResponse();

        for (; iterator.hasNext(); ) {
            String key = iterator.next();
            Object value;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (key.equals("status") && (value instanceof String)) {
                response.setStatus((String) value);
            }
            if (key.equals("code") && (value instanceof String)) {
                response.setCode((String) value);
            }
            if (key.equals("message") && (value instanceof String)){
                response.setMessage((String) value);
            }
        }
        return response;
    }

    public static ArticleResponse parseJsonArticleData(JSONObject jsonObject) {
        Iterator<String> iterator = jsonObject.keys();

        ArticleResponse response = new ArticleResponse();

        for (; iterator.hasNext(); ) {
            String key = iterator.next();
            Object value;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (key.equals("status") && (value instanceof String)) {
                response.setStatus((String) value);
            }
            if (key.equals("totalResults") && (value instanceof Integer)) {
                response.setTotalResults((Integer) value);
            }
            if (key.equals("articles") && (value instanceof JSONArray)){
                response.setArticles(parseJsonListOfArticles((JSONArray) value));
            }
        }
        return response;
    }

    public static List<Article> parseJsonListOfArticles(JSONArray jsonArray) {
        List<Article> articleList = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value;
            try {
                value = jsonArray.get(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (value instanceof JSONObject) {
                articleList.add(parseJsonArticle((JSONObject)value));
            }
        }

        return articleList;
    }

    public static Article parseJsonArticle(JSONObject jsonObject) {
        Article article = new Article();

        Iterator<String> iterator = jsonObject.keys();

        for (; iterator.hasNext(); ) {
            String key = iterator.next();
            Object value;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (key.equals("source") && (value instanceof JSONObject)) {
                article.setSource(parseJsonSource((JSONObject) value));
            }
            if (key.equals("author") && (value instanceof String)) {
                article.setAuthor((String) value);
            }
            if (key.equals("title") && (value instanceof String)) {
                article.setTitle((String) value);
            }
            if (key.equals("description") && (value instanceof String)) {
                article.setDescription((String) value);
            }
            if (key.equals("url") && (value instanceof String)) {
                article.setUrl((String) value);
            }
            if (key.equals("urlToImage") && (value instanceof String)) {
                article.setUrlToImage((String) value);
            }
            if (key.equals("publishedAt") && (value instanceof String)) {
                article.setPublishedAt((String) value);
            }
            if (key.equals("content") && (value instanceof String)) {
                article.setContent((String) value);
            }
        }

        return article;
    }

    public static SourceResponse parseJsonSourceData(JSONObject jsonObject) {
        Iterator<String> iterator = jsonObject.keys();

        SourceResponse response = new SourceResponse();

        for (; iterator.hasNext(); ) {
            String key = iterator.next();
            Object value;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (key.equals("status") && (value instanceof String)) {
                response.setStatus((String) value);
            }
            if (key.equals("sources") && (value instanceof JSONArray)){
                response.setSources(parseJsonListOfSources((JSONArray) value));
            }
        }
        return response;
    }

    public static List<Source> parseJsonListOfSources(JSONArray jsonArray) {
        List<Source> sourceList = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value;
            try {
                value = jsonArray.get(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (value instanceof JSONObject) {
                sourceList.add(parseJsonSource((JSONObject)value));
            }
        }

        return sourceList;
    }

    private static Source parseJsonSource(JSONObject jsonObject) {
        Source source = new Source();
        Iterator<String> iterator = jsonObject.keys();

        for (; iterator.hasNext(); ) {
            String key = iterator.next();
            Object value;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (key.equals("id") && (value instanceof String)) {
                source.setId((String) value);
            }
            if (key.equals("name") && (value instanceof String)) {
                source.setName((String) value);
            }
            if (key.equals("description") && (value instanceof String)) {
                source.setDescription((String) value);
            }
            if (key.equals("url") && (value instanceof String)) {
                source.setUrl((String) value);
            }
            if (key.equals("category") && (value instanceof String)) {
                source.setCategory((String) value);
            }
            if (key.equals("language") && (value instanceof String)) {
                source.setLanguage((String) value);
            }
            if (key.equals("country") && (value instanceof String)) {
                source.setCountry((String) value);
            }
        }

        return source;
    }
}
