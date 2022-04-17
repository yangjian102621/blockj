package org.rockyang.jblock.chain.event;

import org.rockyang.jblock.base.model.Block;
import org.springframework.context.ApplicationEvent;

/**
 * This event will be fired when receive a new block
 *
 * @author yangjian
 */
public class NewBlockEvent extends ApplicationEvent {

	public NewBlockEvent(Block block)
	{
		super(block);
	}
}
