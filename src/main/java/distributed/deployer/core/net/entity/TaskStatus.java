package distributed.deployer.core.net.entity;

public enum TaskStatus {
     create("已创建"), received("已接收"), rendering("执行中"), rendered("执行完成"), sended("回传完成"), fail("失败");

    private String desc;

    TaskStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
