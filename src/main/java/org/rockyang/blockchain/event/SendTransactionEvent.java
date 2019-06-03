package org.rockyang.blockchain.event;

import org.rockyang.blockchain.core.Transaction;
import org.springframework.context.ApplicationEvent;

/**
 * 发送交易事件
 * @author yangjian
 */
public class SendTransactionEvent extends ApplicationEvent {

    public SendTransactionEvent(Transaction transaction) {
        super(transaction);
    }

}
