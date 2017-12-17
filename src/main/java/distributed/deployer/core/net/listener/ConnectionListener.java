package distributed.deployer.core.net.listener;

import distributed.deployer.core.net.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class ConnectionListener implements ChannelFutureListener {
    private Client client;

    public ConnectionListener(Client client) {
        this.client = client;
    }

    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()) {
            System.out.println("Reconnect");
            Thread.sleep(25000);
            client.reConnect();
        }
    }
}