package in.dev.ggs.service;

import in.dev.ggs.model.DocumentStorageModel;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FileStorageService {

    CompletableFuture<String> storeFiles(List<MultipartFile> files);

    CompletableFuture<DocumentStorageModel> fileLister(String filter);

    CompletableFuture<File> getFile(String filename);

    CompletableFuture<File> decryptAndGetFile(String filename, SecretKey key);

    boolean deleteFile(String fileName);
}
