package nc.apps.lab3.model.request;

import lombok.Data;

import java.util.HashMap;

@Data
public class EverythingRequest extends DataRequests{
    private String q, sources, domains, from, to, language, sortBy, pageSize, page;

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
        if (getDomains() != null) {
            mapOfFields.put("domains", getDomains());
        }
        if (getFrom() != null) {
            mapOfFields.put("from", getFrom());
        }
        if (getTo() != null) {
            mapOfFields.put("to", getTo());
        }
        if (getSortBy() != null) {
            mapOfFields.put("sortBy", getSortBy());
        }
        if (getPageSize() != null) {
            mapOfFields.put("pageSize", getPageSize());
        }
        if (getPage() != null) {
            mapOfFields.put("page", getPage());
        }
        return mapOfFields;
    }
}
