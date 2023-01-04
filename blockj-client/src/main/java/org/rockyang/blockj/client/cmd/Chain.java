package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.chain.ChainHead;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class Chain extends Command
{

    public Chain(BlockService service)
    {
        this.name = "chain";
        this.fullName = "chain";
        this.usage = "Interact with blockchain";
        this.blockService = service;
        this.addCommand(new ChainHead(service));
    }

    @Override
    public void action(CliContext context)
    {
        throw new RuntimeException("not implemented");
    }
}
