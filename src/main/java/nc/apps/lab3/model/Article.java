package nc.apps.lab3.model;

import lombok.Data;

@Data
public class Article {
    private Source source;
    private String id;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;
}
