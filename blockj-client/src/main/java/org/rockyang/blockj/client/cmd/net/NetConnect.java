package org.rockyang.blockj.client.cmd.net;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockjService;

/**
 * @author yangjian
 */
public class NetConnect extends Command {

	public NetConnect(BlockjService service)
	{
		this.name = "connect";
		this.usage = "Connect to a peer";
		this.blockService = service;
	}

	@Override
	public void action(CliContext context)
	{
		// TODO: not implemented
	}
}
