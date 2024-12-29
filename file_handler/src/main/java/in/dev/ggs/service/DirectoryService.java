package in.dev.ggs.service;

import in.dev.ggs.model.DirectoryModel;

public interface DirectoryService {

    DirectoryModel saveDirectoryPath(DirectoryModel directoryPath);

    void writeContent(DirectoryModel directoryPath);

    String readFileContent();

    boolean createFile();
}
