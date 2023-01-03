package org.rockyang.blockj.client.rpc.impl;

import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.client.rpc.BlockService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * mock implements for blockjService
 *
 * @author yangjian
 */
public class BlockServiceMock implements BlockService {
	@Override
	public String newWallet()
	{
		return "t1wbh64v24vd6gjtwgu5vpybuvzukx4hoxdmmtzmy";
	}

	@Override
	public List<Wallet> walletList()
	{
		List<Wallet> list = new ArrayList<>(3);
		list.add(new Wallet("t16oicympjfdwwtkzwve6pwgigdw3xbz76k4t66va", new BigDecimal("33124.6542")));
		list.add(new Wallet("t1arfcunbc767a56rvvalmbcdzooc4blqgukctffq", new BigDecimal("1000.3245")));
		list.add(new Wallet("t1wbh64v24vd6gjtwgu5vpybuvzukx4hoxdmmtzmy", new BigDecimal("8675.0976")));
		return list;
	}

	@Override
	public BigDecimal getBalance(String address)
	{
		return BigDecimal.ZERO;
	}

	@Override
	public String sendMessage(String from, String to, BigDecimal value, String param)
	{
		return new Message().getCid();
	}

	@Override
	public Message getMessage(String cid)
	{
		return new Message();
	}

	@Override
	public Long chainHead()
	{
		return Long.valueOf("12345678");
	}
}
