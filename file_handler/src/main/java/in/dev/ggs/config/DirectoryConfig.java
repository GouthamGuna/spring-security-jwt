package in.dev.ggs.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DirectoryConfig {

    @Value("${directoryPath}")
    private String directoryPath;

    @Value("${fileName}")
    private String fileName;

    public String getDirectoryPath() {
        return instance.directoryPath;
    }

    public String getFileName() {
        return instance.fileName;
    }

    private DirectoryConfig instance;

    @PostConstruct
    public void init() {
        instance = this;
    }
}
