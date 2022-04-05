package org.rockyang.jblock.chain.event;

import org.rockyang.jblock.net.base.Peer;
import org.springframework.context.ApplicationEvent;

/**
 * This event will be fired when a new node connected
 * @author yangjian
 */
public class PeerConnectEvent extends ApplicationEvent {

    public PeerConnectEvent(Peer peer) {
        super(peer);
    }
}
