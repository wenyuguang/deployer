package distributed.deployer.demo;

import distributed.deployer.core.net.Client;
import distributed.deployer.core.net.entity.Command;
import distributed.deployer.core.net.entity.EData;
import distributed.deployer.core.net.entity.TaskInfo;
import distributed.deployer.core.utils.EDataUtil;
import distributed.deployer.core.utils.JsonUtil;
import distributed.deployer.core.utils.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class ClientCommandHandler extends SimpleChannelInboundHandler<byte[]> {

    private ChannelHandlerContext ctx;
    private Client client;

    public ClientCommandHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        NettyUtil.ctxWrite(ctx, Command.CONFIG, "clientName=" +Math.random());

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000 * 30);
                    NettyUtil.ctxWrite(ctx, Command.STATUS, "ok=ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, final byte[] msg) {

        EData data = EDataUtil.toEData(msg);
        if (Command.TASK.equals(data.getCommand())) {

            TaskInfo t = JsonUtil.mapper().convertValue(data.getParams(), TaskInfo.class);
            System.out.println(t.getTaskStatus());

        } else if (Command.FILE.equals(data.getCommand())) {

        } else if (Command.STATUS.equals(data.getCommand())) {
            System.out.println(new Date().toString() + "connect is ok!");
        } else if (Command.MESSAGE.equals(data.getCommand())) {
            System.out.println(data.getCommand());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        Thread.sleep(25000);

        Client.connect("127.0.0.1", 2000, ClientCommandHandler::new);

    }


    public void setClient(Client client) {
        this.client = client;
    }
}
