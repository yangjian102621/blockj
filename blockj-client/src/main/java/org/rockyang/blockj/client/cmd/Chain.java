package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.chain.Head;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockjService;

/**
 * @author yangjian
 */
public class Chain extends Command {

	public Chain(BlockjService service)
	{
		this.name = "chain";
		this.usage = "Interact with blockchain";
		this.blockService = service;
		this.addCommand(new Head(service));
	}

	@Override
	public void action(CliContext context)
	{
		throw new RuntimeException("not implemented");
	}
}
