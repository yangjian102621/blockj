package org.rockyang.jblock.event;

import org.rockyang.jblock.chain.Message;
import org.springframework.context.ApplicationEvent;

/**
 * 发送交易事件
 * @author yangjian
 */
public class NewTransactionEvent extends ApplicationEvent {

    public NewTransactionEvent(Message message) {
        super(message);
    }

}
