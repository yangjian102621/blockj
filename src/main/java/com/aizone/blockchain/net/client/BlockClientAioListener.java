package com.aizone.blockchain.net.client;

import com.aizone.blockchain.net.base.BlockPacket;
import com.aizone.blockchain.net.base.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * client端对各个server连接的情况回调。</p>
 * 当某个server的心跳超时（2min）时，Aio会从group里remove掉该连接，需要在重新connect后重新加入group
 *
 * @author yangjian
 */
public class BlockClientAioListener implements ClientAioListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAfterClose(ChannelContext channelContext, Throwable throwable, String s, boolean b) throws Exception {
        logger.info("连接关闭：server地址为-" + channelContext.getServerNode());
        Aio.unbindGroup(channelContext);
    }

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        if (isConnected || isReconnect) {
            logger.info("连接成功：server地址为-" + channelContext.getServerNode());
            Aio.bindGroup(channelContext, Const.GROUP_NAME);
            //重新发送消息
            logger.info("重新发送消息");
            BlockPacket packet = new BlockPacket();
            packet.setBody("Fuck you block Chain.".getBytes(BlockPacket.CHARSET));
            Aio.send(channelContext, packet);
        } else {
            logger.info("连接失败：server地址为-" + channelContext.getServerNode());
        }
    }

    @Override
    public void onAfterReceived(ChannelContext channelContext, Packet packet, int i) throws Exception {

    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean b) throws Exception {

    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String s, boolean b) {

    }
}
