package nc.apps.lab3.service;

import nc.apps.lab3.configs.NewsSettings;
import nc.apps.lab3.model.DataNewsType;
import nc.apps.lab3.model.request.DataRequests;
import nc.apps.lab3.model.request.EverythingRequest;
import nc.apps.lab3.model.request.TopHeadlinesRequest;
import nc.apps.lab3.model.response.DataResponse;
import nc.apps.lab3.util.HttpUtil;
import nc.apps.lab3.util.JSONUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
public class NewsService {

    @Autowired
    NewsSettings newsSettings;

    public DataResponse getNews(DataRequests request, DataNewsType type) {
        String url = newsSettings.getUrl() + newsSettings.getTypeUrl(type);
        String params = getParamsString(request);
        String jsonData = HttpUtil.get(url, params);
        DataResponse response = null;
        try {
            response = JSONUtil.parseJson(new JSONObject(jsonData), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getParamsString(DataRequests request) {
        HashMap<String,String> hashMapOfFields = request.getParamFields();
        String params = "apiKey="+newsSettings.getApiKey();

        for (String key:hashMapOfFields.keySet()) {
            params = params + "&"+key+"="+hashMapOfFields.get(key);
        }
        return params;
    }
}
