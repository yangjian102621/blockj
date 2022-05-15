package org.rockyang.blockj.client.cmd.chain;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;

/**
 * @author yangjian
 */
public class Head extends Command {

	public Head()
	{
		this.name = "head";
		this.usage = "Print chain head";
	}

	@Override
	public void action(CliContext context)
	{
		Long head = blockService.chainHead();
		System.out.println(head);
	}
}
