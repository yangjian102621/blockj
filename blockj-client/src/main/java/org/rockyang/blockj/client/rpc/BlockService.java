package org.rockyang.blockj.client.rpc;

import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Wallet;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yangjian
 */
public interface BlockService {

	String newWallet();

	List<Wallet> walletList();

	BigDecimal getBalance(String address);

	String sendMessage(String from, String to, BigDecimal value, String param);

	Message getMessage(String cid);

	Long chainHead();
}
