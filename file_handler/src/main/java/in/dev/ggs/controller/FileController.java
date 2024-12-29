package in.dev.ggs.controller;

import in.dev.ggs.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/files")
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/stream/{filename:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filename) throws ExecutionException, InterruptedException, FileNotFoundException {

        CompletableFuture<File> cfFile = fileStorageService.getFile(filename);
        File file = cfFile.get();

        String contentType = determineContentType(filename);
        HttpHeaders headers = new HttpHeaders();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        if (contentType.startsWith("video") || contentType.startsWith("audio")) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        } else if (contentType.startsWith("image")) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        } else if (contentType.startsWith("text")) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        } else if (contentType.startsWith("application")) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        } else {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getName());
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    private String determineContentType(String fileName) {

        if (fileName.endsWith(".mp3")) return "audio/mpeg";

        if (fileName.endsWith(".mp4")) return "video/mp4";

        if (fileName.endsWith(".txt")) return "text/plain";

        if (fileName.endsWith(".svg")) return "image/svg+xml";

        if (fileName.endsWith(".png")) return "image/png";

        if ((fileName.endsWith(".jpeg") || fileName.endsWith(".jpg"))) return "image/jpeg";

        if (fileName.endsWith(".json")) return "application/json";

        return "application/octet-stream";
    }
}
