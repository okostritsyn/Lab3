package ok.apps.newsapi.model.response;

import lombok.Data;

@Data
public class ErrorResponse implements DataNews {
    private String status;
    private String code;
    private String message;
}
