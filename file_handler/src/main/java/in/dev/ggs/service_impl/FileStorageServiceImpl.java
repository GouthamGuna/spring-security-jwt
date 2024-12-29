package in.dev.ggs.service_impl;

import in.dev.ggs.config.UrlProvider;
import in.dev.ggs.exception.CustomException;
import in.dev.ggs.model.DocumentStorageModel;
import in.dev.ggs.service.CryptoGraphyService;
import in.dev.ggs.service.DirectoryService;
import in.dev.ggs.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    public static final String DIRECTORY_IS_EMPTY = "Directory is empty.";
    public static final String STRING = "/";
    private final DirectoryService directoryService;
    private final UrlProvider urlProvider;
    private final CryptoGraphyService cryptoGraphyService;
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public FileStorageServiceImpl(DirectoryService directoryService, UrlProvider urlProvider, CryptoGraphyService cryptoGraphyService) {
        this.directoryService = directoryService;
        this.urlProvider = urlProvider;
        this.cryptoGraphyService = cryptoGraphyService;
    }

    @Override
    @Async
    public CompletableFuture<String> storeFiles(List<MultipartFile> files) {
        logger.info("Saving file function invoked");
        var uploadDir = directoryService.readFileContent();

        if (uploadDir != null && !uploadDir.isEmpty()) {

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        Path targetLocation = Paths.get(uploadDir).resolve(Objects.requireNonNull(file.getOriginalFilename()));
                        Files.copy(file.getInputStream(), targetLocation);
                    } catch (IOException ex) {
                        throw new CustomException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
                    }
                }
            }
            return CompletableFuture.completedFuture("Files uploaded successfully!");
        }

        return CompletableFuture.completedFuture("File not saved");
    }

    @Override
    @Async
    public CompletableFuture<DocumentStorageModel> fileLister(String filter) {

        DocumentStorageModel storageModel = new DocumentStorageModel();
        List<String> list = new ArrayList<>();
        String directoryPath = directoryService.readFileContent();

        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] fileList = directory.listFiles();

            switch (filter) {
                case "true" -> generateStreamURLS(fileList, list);
                case "false" -> generateDownloadURLS(fileList, list);
                default -> fetchAllFilesNames(fileList, list);
            }
        } else {
            list.add("Invalid directory path.");
        }
        storageModel.setFiles(list);
        storageModel.setCount(list.size());
        return CompletableFuture.completedFuture(storageModel);
    }

    private void fetchAllFilesNames(File[] fileList, List<String> list) {
        if (fileList != null) {
            for (File file : fileList) {
                list.add(file.getName());
            }
        } else {
            list.add(DIRECTORY_IS_EMPTY);
        }
    }

    private void generateDownloadURLS(File[] fileList, List<String> list) {
        if (fileList != null) {
            for (File file : fileList) {
                list.add("%s%s".formatted(urlProvider.getApiDownloadUrl(), file.getName()));
            }
        } else {
            list.add(DIRECTORY_IS_EMPTY);
        }
    }

    private void generateStreamURLS(File[] fileList, List<String> list) {
        if (fileList != null) {
            for (File file : fileList) {
                list.add("%s%s".formatted(urlProvider.getApiStreamUrl(), file.getName()));
            }
        } else {
            list.add(DIRECTORY_IS_EMPTY);
        }
    }

    @Override
    public CompletableFuture<File> getFile(String filename) {
        File file = new File(directoryService.readFileContent() + File.separator + filename);
        if (!file.exists()) {
            try {
                throw new FileNotFoundException("File not found with name: " + filename);
            } catch (FileNotFoundException e) {
                throw new CustomException(e.getMessage());
            }
        }
        return CompletableFuture.completedFuture(file);
    }

    @Override
    public CompletableFuture<File> decryptAndGetFile(String filename, SecretKey key) {
        return getFile(filename).thenApply(file -> {
            try {
                return cryptoGraphyService.decryptFile(file, key);
            } catch (Exception e) {
                throw new RuntimeException("Failed to decrypt file: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public boolean deleteFile(String fileName) {
        Path path = Paths.get("%s%s%s".formatted(directoryService.readFileContent(), STRING, fileName));
        try {
            Files.delete(path);
            return true;
        } catch (NoSuchFileException e) {
            throw new CustomException("No such file/directory exists");
        } catch (IOException e) {
            throw new CustomException("Invalid permissions");
        }
    }
}
