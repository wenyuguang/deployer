package distributed.deployer.core.net;

import distributed.deployer.core.net.factory.HandlerFactory;
import distributed.deployer.core.net.handler.DataInputDecoder;
import distributed.deployer.core.net.handler.DataOutputEncoder;
import distributed.deployer.core.net.listener.ConnectionListener;
import distributed.deployer.core.utils.NettyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    public String host;
    public int port;
    HandlerFactory hf;

    public static Client connect(String host, int port, HandlerFactory handler) {
        Client client = new Client();
        client.host = host;
        client.port = port;
        client.hf = handler;

        new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup group = new NioEventLoopGroup();
                try {
                    Bootstrap b = new Bootstrap();
                    b.group(group)
                            .channel(NioSocketChannel.class)
                            .handler(NettyUtil.loggingHandler)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast(new DataInputDecoder())
                                            .addLast(new DataOutputEncoder())
                                            .addLast(client.hf.getHandler());
                                }
                            });
                    // Make a new connection.
                    ChannelFuture f = b.connect(host, port).addListener(new ConnectionListener(client)).sync();
                    // Wait until the connection is closed.
                    f.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                }
            }
        }).start();

        return client;
    }

    public void reConnect() {
        connect(this.host, this.port, this.hf);
    }
}
