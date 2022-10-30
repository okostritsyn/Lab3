package ok.apps.newsapi.model.response;

import lombok.Data;
import ok.apps.newsapi.model.Source;

import java.util.List;

@Data
public class SourceResponse implements DataNews {
    private String status;
    private List<Source> sources;
}
