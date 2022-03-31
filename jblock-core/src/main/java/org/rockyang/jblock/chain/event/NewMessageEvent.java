package org.rockyang.jblock.chain.event;

import org.rockyang.jblock.chain.Message;
import org.springframework.context.ApplicationEvent;

/**
 * 发送交易事件
 * @author yangjian
 */
public class NewMessageEvent extends ApplicationEvent {

    public NewMessageEvent(Message message) {
        super(message);
    }

}
