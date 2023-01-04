package org.rockyang.blockj.client.cmd.wallet;

import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class WalletNew extends Command
{

    public WalletNew(BlockService service)
    {
        this.name = "new";
        this.usage = "Generate a new key";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        JsonVo<Wallet> res = blockService.newWallet();
        if (res.isOK()) {
            System.out.println(res.getData());
        } else {
            System.out.println(res.getMessage());
        }

    }
}
