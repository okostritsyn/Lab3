package nc.apps.lab3.service;

import nc.apps.lab3.configs.NewsSettings;
import nc.apps.lab3.model.DataNewsType;
import nc.apps.lab3.model.request.DataRequest;
import nc.apps.lab3.model.response.DataResponse;
import nc.apps.lab3.util.HttpUtil;
import nc.apps.lab3.util.JSONUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NewsService {

    @Autowired
    NewsSettings newsSettings;

    @Async
    public CompletableFuture<DataResponse> getNews(DataRequest request, DataNewsType type) {
        String url = newsSettings.getUrl() + newsSettings.getTypeUrl(type);
        String params = getParamsString(request);
        String jsonData = HttpUtil.get(url, params);
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
        String params = "apiKey="+newsSettings.getApiKey();

        for (String key:hashMapOfFields.keySet()) {
            params = params + "&"+key+"="+hashMapOfFields.get(key);
        }
        return params;
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

}
