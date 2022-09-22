package nc.apps.lab3.model.response;

import nc.apps.lab3.model.request.DataRequest;

@lombok.Data
public class DataResponse {
    private int statusCode;
    private String message;
    private DataRequest request;
    private DataNews data;
}
