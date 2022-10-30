package ok.apps.newsapi.model.response;

import ok.apps.newsapi.model.Article;

import java.util.List;

@lombok.Data
public class ArticleResponse implements DataNews {
    private String status;
    private int totalResults;
    private List<Article> articles;
}
