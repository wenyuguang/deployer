package distributed.deployer.demo;

import distributed.deployer.core.net.entity.*;
import distributed.deployer.core.net.file.FileReceiver;
import distributed.deployer.core.utils.EDataUtil;
import distributed.deployer.core.utils.JsonUtil;
import distributed.deployer.core.utils.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

public class ClientFileReceiveHandler extends SimpleChannelInboundHandler<byte[]> {

    private FileInfo fileInfo;
    private ChannelHandlerContext ctx;

    public ClientFileReceiveHandler(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        NettyUtil.ctxWrite(ctx, Command.FILE, fileInfo);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, final byte[] dataByte) {
        EData d = EDataUtil.toEData(dataByte);
        if (Command.FILE.equals(d.getCommand())) {
            FileInfo f = JsonUtil.mapper().convertValue(d.getParams(), FileInfo.class);
            System.out.println(f.getFileStatus() + "|" + f.getFileName());
            if (FileStatus.create.equals(f.getFileStatus()) || FileStatus.append.equals(f.getFileStatus()) || FileStatus.finish.equals(f.getFileStatus())) {
                try {
                    FileReceiver.receive(d, "d:\\client", fileInfo -> {
                        System.out.println("receive ok! :" + fileInfo.getFileName());
                        NettyUtil.ctxWrite(ctx, Command.TASK, new TaskInfo(fileInfo.getTask(), null, TaskStatus.received));
                        ctx.close();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}