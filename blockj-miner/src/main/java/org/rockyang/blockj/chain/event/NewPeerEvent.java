package org.rockyang.blockj.chain.event;

import org.rockyang.blockj.base.model.Peer;
import org.springframework.context.ApplicationEvent;

/**
 * This event will be fired when a new node connected
 *
 * @author yangjian
 */
public class NewPeerEvent extends ApplicationEvent {

	public NewPeerEvent(Peer peer)
	{
		super(peer);
	}
}
