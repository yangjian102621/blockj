package org.rockyang.blockj.client.cmd.chain;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class ChainHead extends Command {

	public ChainHead(BlockService service)
	{
		this.name = "head";
		this.usage = "Print chain head";
		this.blockService = service;
	}

	@Override
	public void action(CliContext context)
	{
		Long head = blockService.chainHead();
		System.out.println(head);
	}
}
