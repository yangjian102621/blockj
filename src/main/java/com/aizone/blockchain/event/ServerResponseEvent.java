package com.aizone.blockchain.event;

import com.aizone.blockchain.net.base.BlockPacket;
import org.springframework.context.ApplicationEvent;

/**
 * 客户端对外发请求时会触发该Event
 * @author yangjian
 */
public class ServerResponseEvent extends ApplicationEvent {

    public ServerResponseEvent(BlockPacket blockPacket) {
        super(blockPacket);
    }
}
