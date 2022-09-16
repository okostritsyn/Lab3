package nc.apps.lab3.model.response;

@lombok.Data
public class DataResponse {
    private int statusCode;
    private String message;
    private DataNews data;
}
