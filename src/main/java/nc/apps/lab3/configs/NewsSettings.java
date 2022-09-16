package nc.apps.lab3.configs;

import lombok.Data;
import nc.apps.lab3.model.DataNewsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@PropertySource("classpath:application.properties")
@Data
public class NewsSettings {
    private String country;
    private String url;
    private String apiKey;
    private String urlTop;
    private String urlEverything;
    private String urlSources;

    @Autowired
    private Environment environment;

    @PostConstruct
    void postConstruct(){
        setUrl(environment.getRequiredProperty("news.url"));
        setUrlTop(environment.getRequiredProperty("news.urlTop"));
        setUrlEverything(environment.getRequiredProperty("news.urlEverything"));
        setApiKey(environment.getRequiredProperty("news.apiKey"));
        setUrlSources(environment.getRequiredProperty("news.urlSources"));
    }

    public String getTypeUrl(DataNewsType dataType) {
        return dataType == DataNewsType.TOP?this.urlTop:dataType == DataNewsType.ALL?this.urlEverything:this.urlSources;
    }
}
