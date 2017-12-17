package distributed.deployer.demo;

import distributed.deployer.core.net.entity.FileInfo;
import distributed.deployer.core.net.file.FileSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientFileSendHandler extends SimpleChannelInboundHandler<byte[]> {

    private ChannelHandlerContext ctx;
    private FileInfo fileInfo;

    private static Logger log = LoggerFactory.getLogger(ClientFileSendHandler.class);

    public ClientFileSendHandler(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        FileSender.send(ctx, fileInfo.getFilePath()
                , fileInfo
                , f -> {
                    log.debug("send ok: " + fileInfo.getFileName());
                });
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, final byte[] msg) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}