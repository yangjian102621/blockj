package org.rockyang.blockj.client.cmd.wallet;

import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public class WalletBalance extends Command
{

    public WalletBalance(BlockService service)
    {
        this.name = "balance";
        this.fullName = "wallet balance";
        this.usage = "Get account balance";
        this.argsUsage = "[address]";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        String[] args = context.getArgs();
        if (args.length == 0) {
            System.out.println("Please input address");
            return;
        }
        
        JsonVo<BigDecimal> res = blockService.getBalance(args[0]);
        if (res.isOK()) {
            System.out.printf("%-45s%-15s\n", "Address", "Balance");
            System.out.printf("%-45s%-15s\n", args[0], res.getData());
        } else {
            System.out.println(res.getMessage());
        }

    }
}
