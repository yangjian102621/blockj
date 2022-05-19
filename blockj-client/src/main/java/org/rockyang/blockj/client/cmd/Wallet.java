package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.cmd.wallet.WalletList;
import org.rockyang.blockj.client.cmd.wallet.WalletNew;
import org.rockyang.blockj.client.rpc.BlockjService;

/**
 * @author yangjian
 */
public class Wallet extends Command {

	public Wallet(BlockjService service)
	{
		this.name = "wallet";
		this.usage = "Manage wallet";
		this.blockService = service;
		this.addCommand(new WalletNew(service));
		this.addCommand(new WalletList(service));
	}

	@Override
	public void action(CliContext context)
	{

	}
}
