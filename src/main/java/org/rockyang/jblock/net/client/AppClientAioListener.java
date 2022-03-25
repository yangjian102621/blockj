package org.rockyang.jblock.net.client;

import org.rockyang.jblock.db.DBAccess;
import org.rockyang.jblock.net.base.Node;
import org.rockyang.jblock.net.conf.TioProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.client.intf.TioClientListener;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;

/**
 * client端对各个server连接的情况回调。</p>
 * 当某个server的心跳超时（2min）时，Aio会从group里remove掉该连接，需要在重新connect后重新加入group
 *
 * @author yangjian
 */
@Component
public class AppClientAioListener implements TioClientListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    final TioProps tioProps;

    private final DBAccess dbAccess;

    public AppClientAioListener(DBAccess dbAccess, TioProps tioProps)
    {
        this.dbAccess = dbAccess;
        this.tioProps = tioProps;
    }


    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        if (isConnected) {
            logger.info("连接成功：server地址为-" + channelContext.getServerNode());
            Node node = new Node(channelContext.getServerNode().getIp(), channelContext.getServerNode().getPort());
            dbAccess.addNode(node);
            Tio.bindGroup(channelContext, tioProps.getClientGroupName());
        } else {
            logger.info("连接失败：server地址为-" + channelContext.getServerNode());
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
