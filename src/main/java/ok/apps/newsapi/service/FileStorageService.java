package ok.apps.newsapi.service;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("fileStorageService")
public class FileStorageService {

    private final Path path = Paths.get("fileStorage");

    public void init() {
        try {
            File file = path.toFile();
            if (!file.exists()) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for articles files!");
        }
    }

    public Path getPath() {
        return path;
    }

    public Resource load(String filename) {
        Path file = path.resolve(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error:" + e.getMessage());
        }
    }

    public void clear() {
        File file = path.toFile();
        if (file.exists() || file.canWrite()) {
            FileSystemUtils.deleteRecursively(file);
        }
    }

    @PreDestroy
    void preDestroy() {
        clear();
    }

    @PostConstruct
    void postConstruct() {
        clear();
        init();
    }
}
