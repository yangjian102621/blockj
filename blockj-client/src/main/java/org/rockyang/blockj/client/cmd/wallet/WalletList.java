package org.rockyang.blockj.client.cmd.wallet;

import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

import java.util.List;

/**
 * @author yangjian
 */
public class WalletList extends Command
{

    public WalletList(BlockService service)
    {
        this.name = "list";
        this.usage = "List wallet address";
        this.argsUsage = "wallet list";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        JsonVo<List<Wallet>> res = blockService.walletList();
        if (res.isOK()) {
            List<Wallet> wallets = res.getData();
            System.out.printf("%-45s%-15s%-5s\n", "Address", "Balance", "Nonce");
            for (Wallet wallet : wallets) {
                System.out.printf("%-45s%-15s%-5s\n", wallet.getAddress(), wallet.getBalance(), wallet.getMessageNonce());
            }
        } else {
            System.out.println("No wallet on this node");
        }

    }
}
