package distributed.deployer.demo;


import distributed.deployer.core.net.Client;
import distributed.deployer.core.net.Server;
import distributed.deployer.core.net.entity.FileInfo;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import static distributed.deployer.core.net.entity.FileStatus.receive;

@ShellComponent
public class MyCommands {

    @ShellMethod("start  a server ")
    public String start() {
        Server.startServer(2000, ServerCommandHandler::new);
        return "ok";
    }

    @ShellMethod("start a client")
    public String client() {
        Client.connect("10.1.89.126", 2000, ClientCommandHandler::new);
        return "ok";
    }

    @ShellMethod("send a file")
    public String send(String file) {
        FileInfo fileInfo = new FileInfo("file", file, receive);
        Client.connect("10.1.89.126", 2000, () -> new ClientFileSendHandler(fileInfo));
        return "ok";
    }

    @ShellMethod("this 是一个测试命令方法")
    public String test() {
        return new String("this 是一个测试命令方法");
    }

    @ShellMethod("关闭服务")
    public void shutdown(){
        System.out.println( "服务关闭中....");
        System.exit(0);
    }
}
