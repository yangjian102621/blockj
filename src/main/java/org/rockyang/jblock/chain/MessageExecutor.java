package org.rockyang.jblock.chain;

import org.rockyang.jblock.db.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 交易执行器
 * @author yangjian
 * @since 18-4-23
 */
@Component
public class MessageExecutor {

	@Autowired
	private Datastore datastore;

	@Autowired
	private MessagePool messagePool;

	/**
	 * 执行区块中的交易
	 * @param block
	 */
	public void run(Block block) throws Exception {

//		for (Message message : block.getBody().getTransactions())
//		{
//			Optional<Account> recipient = dataStore.getAccount(message.getTo());
//			//如果收款地址账户不存在，则创建一个新账户
//			if (recipient.isEmpty()) {
//				recipient = Optional.of(new Account(message.getTo(), BigDecimal.ZERO));
//			}
//			//挖矿奖励
//			if (null == message.getFrom()) {
//				recipient.get().setBalance(recipient.get().getBalance().add(message.getValue()));
//				dataStore.putAccount(recipient.get());
//				continue;
//			}
//			//账户转账
//			Optional<Account> sender = dataStore.getAccount(message.getFrom());
//			//验证签名
//			boolean verify = Sign.verify(
//					Keys.publicKeyDecode(message.getPublicKey()),
//					message.getSign(),
//					message.toSigned());
//			if (!verify) {
//				message.setStatus(MessageStatus.INVALID_SIGN);
//				continue;
//			}
//			//验证账户余额
//			if (sender.get().getBalance().compareTo(message.getAmount()) == -1) {
//				message.setStatus(MessageStatus.FAIL);
//				message.setErrorMessage("账户余额不足");
//				continue;
//			}
//
//			// 更新交易区块高度
//			message.setBlockNumber(block.getHeader().getIndex());
//			// 缓存交易哈希对应的区块高度, 方便根据 hash 查询交易状态
//			dataStore.put(message.getTxHash(), block.getHeader().getIndex());
//
//			// 将待打包交易池中包含此交易的记录删除，防止交易重复打包( fix bug for #IWSPJ)
//			for (Iterator i = messagePool.getTransactions().iterator(); i.hasNext();) {
//				Message tx = (Message) i.next();
//				if (tx.getTxHash().equals(message.getTxHash())) {
//					i.remove();
//				}
//			}
//
//			//执行转账操作,更新账户余额
//			sender.get().setBalance(sender.get().getBalance().subtract(message.getAmount()));
//			recipient.get().setBalance(recipient.get().getBalance().add(message.getAmount()));
//			dataStore.putAccount(sender.get());
//			dataStore.putAccount(recipient.get());
//		}// end for

		// 更新区块信息
		//dataStore.putBlock(block);
	}
}
