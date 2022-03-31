package org.rockyang.jblock.net.client;

import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.net.conf.TioConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.client.intf.TioClientListener;
import org.tio.core.ChannelContext;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;

/**
 * client端对各个server连接的情况回调。</p>
 * 当某个server的心跳超时（2min）时，Aio会从group里remove掉该连接，需要在重新connect后重新加入group
 *
 * @author yangjian
 */
@Component
public class AppClientListener implements TioClientListener {

    private static final Logger logger = LoggerFactory.getLogger(AppClientListener.class);


    private Datastore datastore;

    public AppClientListener(Datastore datastore)
    {
        this.datastore = datastore;
    }


    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        if (isConnected) {
            logger.info("New node connected: {}", channelContext.getServerNode());
            Node node = new Node(channelContext.getServerNode().getIp(), channelContext.getServerNode().getPort());
            //datastore.addNode(node);
            Tio.bindGroup(channelContext, TioConfig.CLIENT_GROUP_NAME);
            // start sync the block

        } else {
            logger.warn("New node connected failed: {}", channelContext.getServerNode());
        }
    }

    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int i) throws Exception
    {

    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int i) throws Exception
    {

    }


    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean b) throws Exception {

    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long l) throws Exception
    {

    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String s, boolean b) {

    }
}
