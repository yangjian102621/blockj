package org.rockyang.jblock.chain.event;

import org.rockyang.jblock.chain.Block;
import org.springframework.context.ApplicationEvent;

/**
 * 挖矿事件，当挖到一个新的区块将会触发该事件
 * @author yangjian
 */
public class NewBlockEvent extends ApplicationEvent {

    public NewBlockEvent(Block block) {
        super(block);
    }
}
