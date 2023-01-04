package org.rockyang.blockj.client.cmd.chain;

import org.rockyang.blockj.base.model.Block;
import org.rockyang.blockj.base.vo.JsonVo;
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
        this.name = "getBlock";
        this.fullName = "chain getBlock";
        this.usage = "Get a block and print its details";
        this.argsUsage = "[blockIndex]";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        Long height = context.getLongArg(0);
        JsonVo<Block> res = blockService.getBlock(height);
        if (res.isOK()) {
            System.out.println(res.getData());
        } else {
            System.out.println(res.getMessage());
        }
    }
}
