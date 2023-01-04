package org.rockyang.blockj.client.cmd.chain;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class ChainMessage extends Command
{

    public ChainMessage(BlockService service)
    {
        this.name = "getmessage";
        this.argsUsage = "Get and print a message by its cid";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        // TODO: not implemented
    }
}
