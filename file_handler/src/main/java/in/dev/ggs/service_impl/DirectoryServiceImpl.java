package in.dev.ggs.service_impl;

import in.dev.ggs.config.DirectoryConfig;
import in.dev.ggs.model.DirectoryModel;
import in.dev.ggs.service.CryptoGraphyService;
import in.dev.ggs.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    public static final String CONTENT_ENCODING = System.getenv("CONTENT_ENCODING");
    private final DirectoryConfig directoryConfig;
    private final CryptoGraphyService graphyService;
    private final Logger logger = Logger.getLogger(getClass().getName());


    @Autowired
    public DirectoryServiceImpl(DirectoryConfig directoryConfig, CryptoGraphyService graphyService) {
        this.directoryConfig = directoryConfig;
        this.graphyService = graphyService;
    }

    @Override
    public DirectoryModel saveDirectoryPath(DirectoryModel directoryPath) {

        if (createFile()) {
            writeContent(directoryPath);
        }
        directoryPath.setDataStoragePath(readFileContent());
        return directoryPath;
    }

    @Override
    public void writeContent(DirectoryModel directoryPath) {
        try {
            String encryptContent = graphyService.CaesarCipherEncrypt(directoryPath.getDataStoragePath(), CONTENT_ENCODING);
            Files.writeString(getPath(), encryptContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    private Path getPath() {
        return Path.of(directoryConfig.getDirectoryPath() +
                directoryConfig.getFileName()
        );
    }

    @Override
    public String readFileContent() {
        StringBuilder stringBuffer = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(getPath().toString()));
            for (String line : lines) {
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }

        return graphyService.CaesarCipherDecrypt(stringBuffer.toString(), CONTENT_ENCODING);
    }

    @Override
    public boolean createFile() {

        File file = new File(directoryConfig.getDirectoryPath() + directoryConfig.getFileName());

        try {
            if (!file.exists()) {
                return file.createNewFile();
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return true;
    }
}
