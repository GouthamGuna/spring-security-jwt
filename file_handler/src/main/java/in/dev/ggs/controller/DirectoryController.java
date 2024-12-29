package in.dev.ggs.controller;

import in.dev.ggs.model.DirectoryModel;
import in.dev.ggs.model.DocumentStorageModel;
import in.dev.ggs.service.DirectoryService;
import in.dev.ggs.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/directory")
public class DirectoryController {

    private final DirectoryService directoryService;
    private final FileStorageService fileStorageService;

    @Autowired
    public DirectoryController(DirectoryService directoryService,
                               FileStorageService fileStorageService
    ) {
        this.directoryService = directoryService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/save-storage-path")
    public ResponseEntity<DirectoryModel> saveDirectoryPath(@RequestBody DirectoryModel directoryModel) {
        return new ResponseEntity<>(directoryService.saveDirectoryPath(directoryModel), HttpStatus.CREATED);
    }

    @GetMapping("/get-storage-path")
    public ResponseEntity<DirectoryModel> getDirectoryPath() {
        DirectoryModel directoryModel = new DirectoryModel();
        directoryModel.setDataStoragePath(directoryService.readFileContent());
        return new ResponseEntity<>(directoryModel, HttpStatus.OK);
    }

    @PostMapping("/upload-files")
    public CompletableFuture<ResponseEntity<String>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        return fileStorageService.storeFiles(files)
                .thenApply(result -> new ResponseEntity<>(result, HttpStatus.CREATED));
    }

    @GetMapping("/get-files")
    public CompletableFuture<ResponseEntity<DocumentStorageModel>> getFiles(
            @RequestParam(value = "filter", required = false) String filter) {
        if (filter == null) filter = "";
        return fileStorageService.fileLister(filter)
                .thenApply(result -> new ResponseEntity<>(result, HttpStatus.OK));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filename) throws ExecutionException, InterruptedException, FileNotFoundException {
        CompletableFuture<File> file = fileStorageService.getFile(filename);
        File fileData = file.get();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileData));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

        return ResponseEntity.ok().headers(headers)
                .contentLength(fileData.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        boolean isDeleted = fileStorageService.deleteFile(fileName);
        if (isDeleted) {
            return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete file", HttpStatus.NOT_FOUND);
        }
    }
}
