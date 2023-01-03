package org.rockyang.blockj.client.cmd.net;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class NetListen extends Command {

	public NetListen(BlockService service)
	{
		this.name = "listen";
		this.usage = "List listen addresses";
		this.blockService = service;
	}

	@Override
	public void action(CliContext context)
	{
		// TODO: not implemented
	}
}
