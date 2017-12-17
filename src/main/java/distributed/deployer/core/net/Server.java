package distributed.deployer.core.net;

import distributed.deployer.core.net.factory.HandlerFactory;
import distributed.deployer.core.net.handler.DataInputDecoder;
import distributed.deployer.core.net.handler.DataOutputEncoder;
import distributed.deployer.core.utils.NettyUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    public static Map<Integer, Channel> serverMap = new ConcurrentHashMap<>(10);
    public static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    public static EventLoopGroup workerGroup = new NioEventLoopGroup();


    public static void startServer(int port, HandlerFactory hf) {
        new Thread(() -> {
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(NettyUtil.loggingHandler)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline()
                                        .addLast(new DataInputDecoder())
                                        .addLast(new DataOutputEncoder())
                                        .addLast(hf.getHandler());
                            }
                        });

                Channel c = b.bind(port).sync().channel();
                serverMap.put(port, c);
                c.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();

    }

}
