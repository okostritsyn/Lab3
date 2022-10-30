package ok.apps.newsapi.model.request;

import lombok.Data;
import java.util.HashMap;

@Data
public class TopHeadlinesRequest extends DataRequest {
    private String category, sources, q, pageSize, page, country, language;

    @Override
    public HashMap<String,String> getParamFields() {
        HashMap<String,String> mapOfFields = new HashMap<>();
        if (getLanguage() != null) {
            mapOfFields.put("language", getLanguage());
        }
        if (getQ() != null) {
            mapOfFields.put("q", getQ());
        }
        if (getSources() != null) {
            mapOfFields.put("sources", getSources());
        }
        if (getPageSize() != null) {
            mapOfFields.put("pageSize", getPageSize());
        }
        if (getPage() != null) {
            mapOfFields.put("page", getPage());
        }
        if (getCountry() != null) {
            mapOfFields.put("country", getCountry());
        }
        if (getCategory() != null) {
            mapOfFields.put("category", getCategory());
        }
        return mapOfFields;
    }
}
