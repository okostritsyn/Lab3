package nc.apps.lab3.controllers;

import lombok.RequiredArgsConstructor;
import nc.apps.lab3.model.DataNewsType;
import nc.apps.lab3.model.request.EverythingRequest;
import nc.apps.lab3.model.request.SourcesRequest;
import nc.apps.lab3.model.request.TopHeadlinesRequest;
import nc.apps.lab3.model.response.DataResponse;
import nc.apps.lab3.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NewsController {
    @Autowired
    private final NewsService newsService;

    @PostMapping (value = "/top", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse getTopNews(@RequestBody TopHeadlinesRequest request) {
        return newsService.getNews(request, DataNewsType.TOP);
    }

    @PostMapping(value = "/all",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse getAllNews(@RequestBody EverythingRequest request) {
        return newsService.getNews(request,DataNewsType.ALL);
    }

    @PostMapping(value = "/sources",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse getsources(@RequestBody SourcesRequest request) {
        return newsService.getNews(request,DataNewsType.SOURCE);
    }

}
