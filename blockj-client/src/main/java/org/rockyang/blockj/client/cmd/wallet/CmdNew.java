package org.rockyang.blockj.client.cmd.wallet;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockjService;

/**
 * @author yangjian
 */
public class CmdNew extends Command {

	public CmdNew(BlockjService service)
	{
		this.name = "new";
		this.usage = "Generate a new key";
		this.blockService = service;
	}

	@Override
	public void action(CliContext context)
	{
		String address = blockService.newWallet();
		System.out.println(address);
	}
}
