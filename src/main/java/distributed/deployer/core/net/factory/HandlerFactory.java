package distributed.deployer.core.net.factory;

import io.netty.channel.ChannelHandler;

public interface HandlerFactory {
    ChannelHandler getHandler();
}
