package org.rockyang.jblock.chain.event;

import org.springframework.context.ApplicationEvent;

/**
 * 增加区块确认数事件
 * @author yangjian
 */
public class BlockConfirmNumEvent extends ApplicationEvent {

    /**
     * @param blockIndex 区块高度
     */
    public BlockConfirmNumEvent(Integer blockIndex)
    {
        super(blockIndex);
    }
}
