package in.dev.ggs.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlProvider {

    @Value("${api.download.url}")
    private String apiDownloadUrl;

    @Value("${api.stream.url}")
    private String apiStreamUrl;

    public String getApiDownloadUrl() {
        return instance.apiDownloadUrl;
    }

    public String getApiStreamUrl() {
        return instance.apiStreamUrl;
    }

    private UrlProvider instance;

    @PostConstruct
    public void init() {
        instance = this;
    }
}
