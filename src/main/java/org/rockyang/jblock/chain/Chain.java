package org.rockyang.jblock.chain;

import com.google.common.base.Preconditions;
import org.rockyang.jblock.chain.event.NewBlockEvent;
import org.rockyang.jblock.chain.service.AccountService;
import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.chain.service.WalletService;
import org.rockyang.jblock.crypto.Credentials;
import org.rockyang.jblock.crypto.Keys;
import org.rockyang.jblock.crypto.Sign;
import org.rockyang.jblock.enums.MessageStatus;
import org.rockyang.jblock.chain.event.NewMessageEvent;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * @author yangjian
 */
@Component
public class Chain {

	private static final Logger logger = LoggerFactory.getLogger(Chain.class);

	@Qualifier(value = "powMiner")
	private final Miner miner;
	private final MessagePool messagePool;
	private final ChainService chainService;
	private final WalletService walletService;
	private final AccountService accountService;

	public Chain(Miner miner,
	             MessagePool messagePool,
	             ChainService chainService,
	             WalletService walletService,
	             AccountService accountService)
	{
		this.miner = miner;
		this.messagePool = messagePool;
		this.chainService = chainService;
		this.walletService = walletService;
		this.accountService = accountService;
	}

	@PostConstruct
	public void run()
	{
		new Thread(() -> {
			while (true) {
				// get the chain head
				Object chainHead = chainService.chainHead();
				if (chainHead == null) {
					niceSleep(5);
					continue;
				}
				Block preBlock = chainService.getBlock(chainHead);
				if (preBlock == null) {
					niceSleep(5);
					continue;
				}
				BlockHeader preBlockHeader = preBlock.getHeader();
				// check if it's the time for a new round
				if (System.currentTimeMillis() - preBlockHeader.getTimestamp() <= Miner.BLOCK_DELAY_SECS * 1000L) {
					niceSleep(5);
					continue;
				}

				try {
					Block block = miner.mineOne(preBlock);

					logger.info("Mined a new block, Height: {}, Cid: {}", block.getHeader().getHeight(), block.genCid());
					// package the messages in block from message pool
					// @TODO: we should limit the number of messages in each block?
					Iterator<Message> iterator = messagePool.getMessages().iterator();
					while (iterator.hasNext()) {
						Message message = iterator.next();
						block.addMessage(message);
						// remove message from message pool
						iterator.remove();
					}

					// save block and execute messages in block
					saveBlock(block);

					// broadcast the block
					ApplicationContextProvider.publishEvent(new NewBlockEvent(block));
					
				} catch (Exception e) {
					niceSleep(5);
					e.printStackTrace();
				}

			}
		}).start();
	}

	// save block and execute messages in block
	public void saveBlock(Block block) throws Exception
	{
		for (Message message : block.getMessages()) {
			Account recipient = accountService.getAccount(message.getTo());
			// if account is not in local storage, store it.
			if (recipient == null) {
				recipient = new Account(message.getTo(), BigDecimal.ZERO);
			}
			// mining reward message
			if (message.getFrom().equals(Miner.REWARD_ADDR)) {
				recipient.setBalance(recipient.getBalance().add(message.getValue()));
				accountService.setAccount(recipient);
				continue;
			}

			// transfer balance
			Account sender = accountService.getAccount(message.getFrom());
			if (sender == null) {
				logger.info("Key not exists {}", message.getFrom());
				continue;
			}
			// check the sign
			boolean verify = Sign.verify(
					Keys.publicKeyDecode(message.getPublicKey()),
					message.getSign(),
					message.toSigned());
			if (!verify) {
				message.setStatus(MessageStatus.INVALID_SIGN);
				continue;
			}
			// check if the account had enough balance
			if (sender.getBalance().compareTo(message.getValue()) < 0) {
				message.setStatus(MessageStatus.INSUFFICIENT_BALANCE);
				continue;
			}

			// update the message height
			message.setHeight(block.getHeader().getHeight());
			message.setStatus(MessageStatus.SUCCESS);

			// Perform transfer operations and update account balances
			sender.setBalance(sender.getBalance().subtract(message.getValue()));
			recipient.setBalance(recipient.getBalance().add(message.getValue()));
			accountService.setAccount(sender);
			accountService.setAccount(recipient);
		}
		chainService.addBlock(block);
		chainService.setChainHead(block.getHeader().getHeight());
	}

	public void niceSleep(int secs)
	{
		try {
			Thread.sleep(secs * 1000L);
		} catch (InterruptedException e) {
			logger.warn("received interrupt while trying to sleep in mining cycle");
		}
	}


	public Message sendMessage(Credentials credentials, String to, BigDecimal amount, String param) throws Exception
	{

		//校验付款和收款地址
		Preconditions.checkArgument(to.startsWith("0x"), "收款地址格式不正确");
		Preconditions.checkArgument(!credentials.getAddress().equals(to), "收款地址不能和发送地址相同");

		//构建交易对象
		Message message = new Message(credentials.getAddress(), to, amount);
		message.setStatus(MessageStatus.APPENDING);
		message.setParams(param);
		message.setCid(message.genMsgCid());
		//签名
		String sign = Sign.sign(credentials.getEcKeyPair().getPrivateKey(), message.toSigned());
		message.setSign(sign);

		//先验证私钥是否正确
		if (!Sign.verify(credentials.getEcKeyPair().getPublicKey(), sign, message.toSigned())) {
			throw new RuntimeException("私钥签名验证失败，非法的私钥");
		}
		// 加入交易池，等待打包
		messagePool.pendingMessage(message);

		ApplicationContextProvider.publishEvent(new NewMessageEvent(message));
		return message;
	}
}
