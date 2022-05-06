package org.rockyang.jblock.client.rpc;

import org.rockyang.jblock.base.model.Message;
import org.rockyang.jblock.base.model.Wallet;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yangjian
 */
public interface JBlockService {

	String newWallet();

	List<Wallet> walletList();

	BigDecimal getBalance(String address);

	String sendMessage(String from, String to, BigDecimal value, String param);

	Message getMessage(String cid);

	Long chainHead();
}
