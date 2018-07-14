package com.ppblock.blockchain.event;

import com.ppblock.blockchain.core.Transaction;
import org.springframework.context.ApplicationEvent;

/**
 * 交易事件
 * @author yangjian
 */
public class SendTransactionEvent extends ApplicationEvent {

    public SendTransactionEvent(Transaction transaction) {
        super(transaction);
    }

}
