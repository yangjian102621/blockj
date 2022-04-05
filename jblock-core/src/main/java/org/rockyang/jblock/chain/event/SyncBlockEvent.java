package org.rockyang.jblock.chain.event;

import org.springframework.context.ApplicationEvent;

/**
 * This event will be fired when receive a request to synchronize the next block
 * @author yangjian
 */
public class SyncBlockEvent extends ApplicationEvent {

    public SyncBlockEvent(Integer blockIndex) {
        super(blockIndex);
    }
}
