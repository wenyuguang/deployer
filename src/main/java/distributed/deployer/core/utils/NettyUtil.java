package distributed.deployer.core.utils;

import distributed.deployer.core.net.entity.Command;
import distributed.deployer.core.net.entity.EData;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;


public class NettyUtil {
    private static Logger logger = LoggerFactory.getLogger(NettyUtil.class);

    /**
     * @param ctx
     * @param command
     * @param body
     * @param param   format ["a=b", ...]
     * @return
     */
    public static ChannelFuture ctxWrite(ChannelHandlerContext ctx, Command command, byte[] body, String... param) {
        Map<String, String> paramMap = new LinkedHashMap<>(param.length);
        for (int i = 0; i < param.length; i++) {
            paramMap.put(param[i].split("=")[0], param[i].split("=")[1]);
        }
        return ctx.writeAndFlush(EDataUtil.eDataToBytes(new EData(command, paramMap, body)));
    }

    /**
     *
     * @param ctx
     * @param command
     * @param param
     * @return
     */
    public static ChannelFuture ctxWrite(ChannelHandlerContext ctx, Command command, String... param) {
        return ctxWrite(ctx, command, null, param);
    }

    public static ChannelFuture ctxWrite(ChannelHandlerContext ctx, Command command, String param) {
        return ctxWrite(ctx, command, null, new String[]{param});
    }

    public static ChannelFuture ctxWrite(ChannelHandlerContext ctx, Command command, Object param) {
        return ctx.writeAndFlush(EDataUtil.eDataToBytes(new EData(command, param, null)));
    }

    public static ChannelFuture ctxWrite(ChannelHandlerContext ctx, Command command, Object param, byte[] body) {
        return ctx.writeAndFlush(EDataUtil.eDataToBytes(new EData(command, param, body)));
    }

    public static LoggingHandler loggingHandler = new LoggingHandler(
            LogLevel.INFO);

}
