package com.ppblock.blockchain.listener;

import com.ppblock.blockchain.account.Account;
import com.ppblock.blockchain.event.NewAccountEvent;
import com.ppblock.blockchain.net.base.MessagePacket;
import com.ppblock.blockchain.net.base.MessagePacketType;
import com.ppblock.blockchain.net.client.AppClient;
import com.ppblock.blockchain.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 账户事件监听器
 * @author yangjian
 * @since 2018-04-21 下午12:02.
 */
@Component
public class AccountEventListener {

	private static Logger logger = LoggerFactory.getLogger(AccountEventListener.class);

	@Autowired
	private AppClient appClient;

	@EventListener(NewAccountEvent.class)
	public void newAccount(NewAccountEvent event) {

		Account account = (Account) event.getSource();
		logger.info("准备发起账户同步请求， {}", account);
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_NEW_ACCOUNT);
		messagePacket.setBody(SerializeUtils.serialize(account));
		appClient.sendGroup(messagePacket);
	}
}
