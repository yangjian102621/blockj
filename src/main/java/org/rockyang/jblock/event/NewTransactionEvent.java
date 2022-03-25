package org.rockyang.jblock.event;

import org.rockyang.jblock.core.Transaction;
import org.springframework.context.ApplicationEvent;

/**
 * 发送交易事件
 * @author yangjian
 */
public class NewTransactionEvent extends ApplicationEvent {

    public NewTransactionEvent(Transaction transaction) {
        super(transaction);
    }

}
