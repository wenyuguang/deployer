package distributed.deployer.demo;

import distributed.deployer.core.net.entity.Command;
import distributed.deployer.core.net.entity.EData;
import distributed.deployer.core.net.entity.FileInfo;
import distributed.deployer.core.net.entity.FileStatus;
import distributed.deployer.core.net.file.FileReceiver;
import distributed.deployer.core.utils.EDataUtil;
import distributed.deployer.core.utils.JsonUtil;
import distributed.deployer.core.utils.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerCommandHandler extends SimpleChannelInboundHandler<byte[]> {

    private static Logger log = LoggerFactory.getLogger(ServerCommandHandler.class);

    public ServerCommandHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        EData data = EDataUtil.toEData(msg);
        if (Command.CONFIG.equals(data.getCommand())) {
            String clientName = data.getParams().get("clientName").toString();
            log.info(">>>>" + clientName);
        } else if (Command.TASK.equals(data.getCommand())) {
        } else if (Command.FILE.equals(data.getCommand())) {
            FileInfo f = JsonUtil.mapper().convertValue(data.getParams(), FileInfo.class);
            log.info(">>>>" + f.getFileStatus() + "|" + f.getFileName());
            if (FileStatus.receive.equals(f.getFileStatus())) {
            } else if (FileStatus.create.equals(f.getFileStatus()) || FileStatus.append.equals(f.getFileStatus()) || FileStatus.finish.equals(f.getFileStatus())) {
                FileReceiver.receive(data, "d:\\serverFile\\", fi -> {
                    ctx.close();
                    log.info(fi.getFileName() + " receive ok!");

                });
            }


        } else if (Command.MESSAGE.equals(data.getCommand())) {

        } else if (Command.STATUS.equals(data.getCommand())) {
            NettyUtil.ctxWrite(ctx, Command.STATUS, "ok=ok");
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
