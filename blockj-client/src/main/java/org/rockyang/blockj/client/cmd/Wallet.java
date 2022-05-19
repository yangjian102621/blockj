package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.cmd.wallet.CmdList;
import org.rockyang.blockj.client.cmd.wallet.CmdNew;
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
		this.addCommand(new CmdNew(service));
		this.addCommand(new CmdList(service));
	}

	@Override
	public void action(CliContext context)
	{

	}
}
