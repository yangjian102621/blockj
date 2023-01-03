package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.net.NetConnect;
import org.rockyang.blockj.client.cmd.net.NetListen;
import org.rockyang.blockj.client.cmd.net.NetPeers;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class Net extends Command {

	public Net(BlockService service)
	{
		this.name = "net";
		this.usage = "Manage P2P Network";
		this.blockService = service;
		this.addCommand(new NetPeers(service));
		this.addCommand(new NetListen(service));
		this.addCommand(new NetConnect(service));
	}

	@Override
	public void action(CliContext context)
	{
	}
}
