package com.aizone.blockchain.event;

import com.aizone.blockchain.wallet.Account;
import org.springframework.context.ApplicationEvent;

/**
 * 创建账户事件
 * @author yangjian
 */
public class NewAccountEvent extends ApplicationEvent {

    public NewAccountEvent(Account account) {
        super(account);
    }
}
