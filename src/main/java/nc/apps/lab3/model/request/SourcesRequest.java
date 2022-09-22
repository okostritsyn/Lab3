package nc.apps.lab3.model.request;

import lombok.Data;

import java.util.HashMap;

@Data
public class SourcesRequest extends DataRequest {
    private String category, language, country;

    @Override
    public HashMap<String,String> getParamFields() {
        HashMap<String,String> mapOfFields = new HashMap<>();
        if (getCategory()!=null) {
            mapOfFields.put("category", getCategory());
        }
        if (getLanguage()!=null) {
            mapOfFields.put("language", getLanguage());
        }
        if (getCountry()!=null) {
            mapOfFields.put("country", getCountry());
        }
        return mapOfFields;
    }
}
