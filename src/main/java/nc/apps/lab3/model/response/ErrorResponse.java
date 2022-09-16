package nc.apps.lab3.model.response;

import lombok.Data;

@Data
public class ErrorResponse implements DataNews {
    private String status;
    private String code;
    private String message;
}
