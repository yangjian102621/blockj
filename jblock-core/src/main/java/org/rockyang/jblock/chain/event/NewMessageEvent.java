package org.rockyang.jblock.chain.event;

import org.rockyang.jblock.chain.Message;
import org.springframework.context.ApplicationEvent;

/**
 * This event will be fired when receive a new message
 * @author yangjian
 */
public class NewMessageEvent extends ApplicationEvent {

    public NewMessageEvent(Message message) {
        super(message);
    }

}
