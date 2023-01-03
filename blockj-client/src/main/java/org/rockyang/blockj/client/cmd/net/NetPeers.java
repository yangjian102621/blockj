package org.rockyang.blockj.client.cmd.net;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class NetPeers extends Command {

	public NetPeers(BlockService service)
	{
		this.name = "peers";
		this.usage = "Print peers";
		this.blockService = service;
	}

	@Override
	public void action(CliContext context)
	{
		// TODO: not implemented
	}
}
