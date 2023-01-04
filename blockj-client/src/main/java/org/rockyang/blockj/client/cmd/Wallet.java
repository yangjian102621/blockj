package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.cmd.wallet.WalletBalance;
import org.rockyang.blockj.client.cmd.wallet.WalletList;
import org.rockyang.blockj.client.cmd.wallet.WalletNew;
import org.rockyang.blockj.client.rpc.BlockService;

/**
 * @author yangjian
 */
public class Wallet extends Command
{

    public Wallet(BlockService service)
    {
        this.name = "wallet";
        this.fullName = "wallet";
        this.usage = "Manage wallet";
        this.blockService = service;
        this.addCommand(new WalletNew(service));
        this.addCommand(new WalletList(service));
        this.addCommand(new WalletBalance(service));
    }

    @Override
    public void action(CliContext context)
    {

    }
}
