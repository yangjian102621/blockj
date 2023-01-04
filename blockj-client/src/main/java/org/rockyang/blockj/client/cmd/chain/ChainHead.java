package org.rockyang.blockj.client.cmd.chain;

import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class ChainHead extends Command
{

    public ChainHead(BlockService service)
    {
        this.name = "head";
        this.fullName = "chain head";
        this.argsUsage = "Print chain head";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        JsonVo<Long> res = blockService.chainHead();
        if (res.isOK()) {
            System.out.printf("Chain head: %d%n", res.getData());
        } else {
            System.out.println(res.getMessage());
        }
    }
}
