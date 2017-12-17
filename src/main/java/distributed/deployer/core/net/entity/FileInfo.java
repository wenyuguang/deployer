package distributed.deployer.core.net.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.nio.file.Paths;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfo {
    private String task;
    private String filePath;
    private FileStatus fileStatus;//create  append finish

    public FileInfo() {
    }

    public FileInfo(String task, String filePath, FileStatus operate) {
        this.task = task;
        this.filePath = filePath;
        this.fileStatus = operate;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getFileName() {
        return Paths.get(filePath).getFileName().toString();
    }


    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
