package nc.apps.lab3.controllers;

import nc.apps.lab3.service.FileStorageService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import nc.apps.lab3.model.Article;
import nc.apps.lab3.model.DataNewsType;
import nc.apps.lab3.model.request.DataRequest;
import nc.apps.lab3.model.request.EverythingRequest;
import nc.apps.lab3.model.request.SourcesRequest;
import nc.apps.lab3.model.request.TopHeadlinesRequest;
import nc.apps.lab3.model.response.DataResponse;
import nc.apps.lab3.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class NewsController {
    @Autowired
    private final NewsService newsService;

    @Autowired
    private final FileStorageService fileStorageService;

    @PostMapping (value = "/top", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DataResponse> getTopNews(@RequestBody List<TopHeadlinesRequest> requestList) throws Throwable {
        List<DataRequest> dataRequestList = new ArrayList<>(requestList.size());
        for (TopHeadlinesRequest request:requestList) {
            dataRequestList.add(request);
        }
        return newsService.getNewsList(dataRequestList,DataNewsType.TOP);
    }

    @PostMapping(value = "/all",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DataResponse> getAllNews(@RequestBody List<EverythingRequest> requestList) throws Throwable {
        List<DataRequest> dataRequestList = new ArrayList<>(requestList.size());
        for (EverythingRequest request:requestList) {
            dataRequestList.add(request);
        }
        return newsService.getNewsList(dataRequestList,DataNewsType.ALL);
    }

    @PostMapping(value = "/getfile",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFile(@RequestBody Article article) throws IOException {
        String filename = newsService.writeArticleToFile(article);
        return ResponseEntity.ok()
                .body(filename);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable("filename")String filename){
        Resource file = fileStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @PostMapping(value = "/sources",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DataResponse> getsources(@RequestBody List<SourcesRequest> requestList) throws Throwable {
        List<DataRequest> dataRequestList = new ArrayList<>(requestList.size());
        for (SourcesRequest request:requestList) {
            dataRequestList.add(request);
        }
        return newsService.getNewsList(dataRequestList,DataNewsType.SOURCE);
    }
}
