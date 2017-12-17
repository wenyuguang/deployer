package distributed.deployer.core.net.entity;

public class TaskInfo {
    private String name;
    private TaskStatus taskStatus;
    private String ae;

    public TaskInfo() {
    }

    public TaskInfo(String name, String ae, TaskStatus taskStatus) {
        this.name = name;
        this.taskStatus = taskStatus;
        this.ae = ae;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getAe() {
        return this.ae;
    }

    public void setAe(String ae) {
        this.ae = ae;
    }
}
