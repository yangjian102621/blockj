package org.rockyang.blockj.chain.event;

import org.rockyang.blockj.base.model.Block;
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
