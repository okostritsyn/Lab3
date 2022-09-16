package nc.apps.lab3.model.response;

import lombok.Data;
import nc.apps.lab3.model.Source;

import java.util.List;

@Data
public class SourceResponse implements DataNews {
    private String status;
    private List<Source> sources;
}
