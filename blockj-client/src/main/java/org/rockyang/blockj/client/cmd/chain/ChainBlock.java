package org.rockyang.blockj.client.cmd.chain;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class ChainBlock extends Command
{

    public ChainBlock(BlockService service)
    {
        this.name = "getblock";
        this.argsUsage = "Get a block and print its details";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        // TODO: not implemented
    }
}
