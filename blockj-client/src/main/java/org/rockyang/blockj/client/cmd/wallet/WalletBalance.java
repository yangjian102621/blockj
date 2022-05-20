package org.rockyang.blockj.client.cmd.wallet;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockjService;

/**
 * @author yangjian
 */
public class WalletBalance extends Command {

	public WalletBalance(BlockjService service)
	{
		this.name = "balance";
		this.usage = "Get account balance";
		this.blockService = service;
	}

	@Override
	public void action(CliContext context)
	{
		String address = blockService.newWallet();
		System.out.println(address);
	}
}
