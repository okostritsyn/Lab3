package nc.apps.lab3.service;

import nc.apps.lab3.configs.NewsSettings;
import nc.apps.lab3.model.Article;
import nc.apps.lab3.model.DataNewsType;
import nc.apps.lab3.model.request.DataRequest;
import nc.apps.lab3.model.response.DataResponse;
import nc.apps.lab3.util.HttpUtil;
import nc.apps.lab3.util.JSONUtil;
import nc.apps.lab3.util.WordDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
