package org.rockyang.blockchain.event;

import org.rockyang.blockchain.core.Block;
import org.rockyang.blockchain.core.Block;
import org.springframework.context.ApplicationEvent;

/**
 * 挖矿事件，当挖到一个新的区块将会触发该事件
 * @author yangjian
 */
public class MineBlockEvent extends ApplicationEvent {

    public MineBlockEvent(Block block) {
        super(block);
    }
}
