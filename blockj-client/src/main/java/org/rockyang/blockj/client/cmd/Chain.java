package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.impl.BlockjServiceMock;

import java.util.HashMap;

/**
 * @author yangjian
 */
public class Chain extends Command {

	public Chain()
	{
		this.name = "chain";
		this.usage = "Interact with blockj blockchain";
		this.blockService = new BlockjServiceMock();
		this.subCommands = new HashMap<>(8);
	}

	@Override
	public void action(CliContext context)
	{
		System.out.println(context.getArgs());
	}
}
