package org.rockyang.jblock.chain.event;

import org.rockyang.jblock.net.base.Peer;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author yangjian
 */
public class NodeConnectEvent extends ApplicationEvent {

    public NodeConnectEvent(Peer peer) {
        super(peer);
    }
}
