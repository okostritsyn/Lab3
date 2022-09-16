package nc.apps.lab3.model.response;

import nc.apps.lab3.model.Article;

import java.util.List;

@lombok.Data
public class ArticleResponse implements DataNews {
    private String status;
    private int totalResults;
    private List<Article> articles;
}
