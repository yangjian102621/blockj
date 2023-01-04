package org.rockyang.blockj.client.cmd.chain;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.vo.JsonVo;
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
        this.name = "getMessage";
        this.fullName = "chain getMessage";
        this.usage = "Get and print a message by its cid";
        this.argsUsage = "[MessageCid]";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        String cid = context.getArg(0);
        if (StringUtils.isBlank(cid)) {
            System.out.println("Please input the message Cid.");
        }

        JsonVo<Message> res = blockService.getMessage(cid);
        if (res.isOK()) {
            System.out.println(res.getData());
        } else {
            System.out.println(res.getMessage());
        }
    }
}
