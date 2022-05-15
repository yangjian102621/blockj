package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.chain.Head;
import org.rockyang.blockj.client.cmd.utils.CliContext;

/**
 * @author yangjian
 */
public class Chain extends Command {

	public Chain()
	{
		this.name = "chain";
		this.usage = "Interact with blockchain";
		this.addCommand(new Head());
	}

	@Override
	public void action(CliContext context)
	{
		throw new RuntimeException("not implemented");
	}
}
