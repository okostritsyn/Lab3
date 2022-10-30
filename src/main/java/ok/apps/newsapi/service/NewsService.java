package ok.apps.newsapi.service;

import ok.apps.newsapi.configs.NewsSettings;
import ok.apps.newsapi.model.Article;
import ok.apps.newsapi.model.DataNewsType;
import ok.apps.newsapi.model.request.DataRequest;
import ok.apps.newsapi.model.response.DataResponse;
import ok.apps.newsapi.util.HttpUtil;
import ok.apps.newsapi.util.JSONUtil;
import ok.apps.newsapi.util.WordDocument;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NewsService {

    @Autowired
    NewsSettings newsSettings;

    @Autowired
    HttpUtil httpUtil;

    @Autowired
    FileStorageService fileStorageService;

    @Async
    public CompletableFuture<DataResponse> getNews(DataRequest request, DataNewsType type) {
        String url = newsSettings.getUrl() + newsSettings.getTypeUrl(type);
        String params = getParamsString(request);
        String jsonData = httpUtil.get(url, params, url+params);
        DataResponse response = null;
        try {
            response = JSONUtil.parseJson(new JSONObject(jsonData), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.setRequest(request);
        return CompletableFuture.completedFuture(response);
    }

    private String getParamsString(DataRequest request) {
        HashMap<String,String> hashMapOfFields = request.getParamFields();
        StringBuilder params = new StringBuilder("apiKey=" + newsSettings.getApiKey());

        for (String key:hashMapOfFields.keySet()) {
            params.append("&").append(key).append("=").append(hashMapOfFields.get(key));
        }
        return params.toString();
    }

    public List<DataResponse> getNewsList(List<DataRequest> requestList,DataNewsType type) throws Throwable {
        List<DataResponse> responseList = new ArrayList<>(requestList.size());
        List<CompletableFuture<DataResponse>> responseFutureList = new ArrayList<>(requestList.size());

        for (DataRequest request:requestList) {
            CompletableFuture<DataResponse> response = getNews(request,type);
            responseFutureList.add(response);
        }

        for (CompletableFuture<DataResponse> resp:responseFutureList) {
            try {
                responseList.add(resp.get());
            } catch (Throwable e) {
                throw e.getCause();
            }
        }
        return responseList;
    }

    public String writeArticleToFile(Article article) throws IOException {
        Path path = fileStorageService.getPath();

        File newFile = new File(path.toString(), "Article_"+article.getId()+".doc");
        try {
            if (newFile.exists()) {
                newFile.delete();
            }
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String imageName = null;
        try {
            imageName = httpUtil.saveImage(article.getUrlToImage(),path,article.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        WordDocument wordDocument = new WordDocument();
        wordDocument.addTitle(article.getAuthor());
        wordDocument.addSubTitle(article.getTitle());
        wordDocument.addText(article.getDescription());

        Path imagePath = fileStorageService.getPath().resolve(imageName);
        /*try {
            wordDocument.addImage(imagePath);
        } catch (InvalidFormatException | URISyntaxException e) {
            e.printStackTrace();
        }*/
        wordDocument.writeToFile(newFile.getAbsolutePath());

        return newFile.getName();
    }
}
