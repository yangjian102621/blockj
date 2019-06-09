package org.rockyang.blockchain.core;

import com.google.common.base.Optional;
import org.rockyang.blockchain.account.Account;
import org.rockyang.blockchain.crypto.Keys;
import org.rockyang.blockchain.crypto.Sign;
import org.rockyang.blockchain.db.DBAccess;
import org.rockyang.blockchain.enums.TransactionStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * 交易执行器
 * @author yangjian
 * @since 18-4-23
 */
@Component
public class TransactionExecutor {

	@Autowired
	private DBAccess dbAccess;

	@Autowired
	private TransactionPool transactionPool;

	/**
	 * 执行区块中的交易
	 * @param block
	 */
	public void run(Block block) throws Exception {

		for (Transaction transaction : block.getBody().getTransactions())
		{
			Optional<Account> recipient = dbAccess.getAccount(transaction.getTo());
			//如果收款地址账户不存在，则创建一个新账户
			if (!recipient.isPresent()) {
				recipient = Optional.of(new Account(transaction.getTo(), BigDecimal.ZERO));
			}
			//挖矿奖励
			if (null == transaction.getFrom()) {
				recipient.get().setBalance(recipient.get().getBalance().add(transaction.getAmount()));
				dbAccess.putAccount(recipient.get());
				continue;
			}
			//账户转账
			Optional<Account> sender = dbAccess.getAccount(transaction.getFrom());
			//验证签名
			boolean verify = Sign.verify(
					Keys.publicKeyDecode(transaction.getPublicKey()),
					transaction.getSign(),
					transaction.toSignString());
			if (!verify) {
				transaction.setStatus(TransactionStatusEnum.FAIL);
				transaction.setErrorMessage("交易签名错误");
				continue;
			}
			//验证账户余额
			if (sender.get().getBalance().compareTo(transaction.getAmount()) == -1) {
				transaction.setStatus(TransactionStatusEnum.FAIL);
				transaction.setErrorMessage("账户余额不足");
				continue;
			}

			// 更新交易区块高度
			transaction.setBlockNumber(block.getHeader().getIndex());
			// 缓存交易哈希对应的区块高度, 方便根据 hash 查询交易状态
			dbAccess.put(transaction.getTxHash(), block.getHeader().getIndex());

			// 将待打包交易池中包含此交易的记录删除，防止交易重复打包( fix bug for #IWSPJ)
			for (Iterator i = transactionPool.getTransactions().iterator(); i.hasNext();) {
				Transaction tx = (Transaction) i.next();
				if (tx.getTxHash().equals(transaction.getTxHash())) {
					i.remove();
				}
			}

			//执行转账操作,更新账户余额
			sender.get().setBalance(sender.get().getBalance().subtract(transaction.getAmount()));
			recipient.get().setBalance(recipient.get().getBalance().add(transaction.getAmount()));
			dbAccess.putAccount(sender.get());
			dbAccess.putAccount(recipient.get());
		}// end for

		// 更新区块信息
		dbAccess.putBlock(block);
	}
}
