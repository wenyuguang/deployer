package distributed.deployer.core.net.file;

import distributed.deployer.core.net.entity.Command;
import distributed.deployer.core.net.entity.EData;
import distributed.deployer.core.net.entity.FileInfo;
import distributed.deployer.core.net.entity.FileStatus;
import distributed.deployer.core.utils.EDataUtil;
import distributed.deployer.core.utils.NettyUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileSender {
    private FileInfo fileInfo;
    private String filePath = null;
    private File file = null;
    private InputStream fi = null;
    private int length = 1024 * 1024 * 2;
    private byte[] bs = new byte[length];
    private ChannelHandlerContext ctx;

    private FinishCallBack finishFunction;

    private static Logger log = LoggerFactory.getLogger(FileSender.class);

    private FileSender() {
    }

    public FileSender(FileInfo fileInfo, String filePath, ChannelHandlerContext ctx, FinishCallBack finishFunction) {
        this.fileInfo = fileInfo;
        this.filePath = filePath;
        this.ctx = ctx;
        this.finishFunction = finishFunction;
    }

    public static void send(ChannelHandlerContext ctx, String filePath, FileInfo fileInfo, FinishCallBack finishFunction) {
        FileSender f = new FileSender(fileInfo, filePath, ctx, finishFunction);
        log.info(filePath);
        f.file = new File(filePath);
        try {
            f.fi = new FileInputStream(f.file);
            f.doSend();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void send(ChannelHandlerContext ctx, File file, FileInfo fileInfo, FinishCallBack finishFunction) {
        FileSender f = new FileSender(fileInfo, file.getPath(), ctx, finishFunction);
        f.file = file;
        try {
            f.fi = new FileInputStream(f.file);
            f.doSend();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void send(ChannelHandlerContext ctx, InputStream inputStream, FileInfo fileInfo, FinishCallBack finishFunction) {
        FileSender f = new FileSender(fileInfo, null, ctx, finishFunction);
        f.ctx = ctx;
        f.fi = inputStream;
        f.doSend();
    }

    private void doSend() {
        fileInfo.setFileStatus(FileStatus.create);
        NettyUtil.ctxWrite(ctx, Command.FILE, fileInfo);
        readFile();
    }

    private void readFile() {
        try {
            int read = IOUtils.read(fi, bs);
            if (read > 0) {
                fileInfo.setFileStatus(FileStatus.append);
                ctx.writeAndFlush(
                        EDataUtil.eDataToBytes(new EData(Command.FILE, fileInfo, bs), read)
                ).addListener(new FileChannelListen());
            } else {
                finishSend();
            }
        } catch (IOException e) {
            e.printStackTrace();
            finishSend();
        }
    }

    private void finishSend() {

        try {
            fi.close();
            finishFunction.accept(fileInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileInfo.setFileStatus(FileStatus.finish);
        NettyUtil.ctxWrite(ctx, Command.FILE, fileInfo);

    }

    class FileChannelListen implements ChannelFutureListener {
        @Override
        public void operationComplete(ChannelFuture future) {
            if (future.isSuccess()) {
                readFile();
            }
        }
    }


}
