package org.rockyang.blockj.client.cmd.net;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class NetConnect extends Command
{

    public NetConnect(BlockService service)
    {
        this.name = "connect";
        this.usage = "Connect to a peer";
        this.argsUsage = "[peerAddress]";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        throw new RuntimeException("Not implemented");
    }
}
