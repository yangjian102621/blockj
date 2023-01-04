package org.rockyang.blockj.client.cmd.wallet;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.base.vo.MnemonicWallet;
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
        Boolean mnemonic = context.getBool("mnemonic");
        JsonVo res;
        if (mnemonic) {
            String password = context.getArg(0);
            if (StringUtils.isBlank(password)) {
                System.out.println("Mnemonic wallet need a password");
                return;
            }
            res = blockService.newMnemonicWallet(password);
        } else {
            res = blockService.newWallet();
        }
        if (res.isOK()) {
            if (mnemonic) {
                MnemonicWallet wallet = (MnemonicWallet) res.getData();
                System.out.printf("Mnemonic words: %s%n", wallet.getMnemonic());
                System.out.printf("Address: %s%n", wallet.getWallet().getAddress());
            } else {
                Wallet wallet = (Wallet) res.getData();
                System.out.println(wallet.getAddress());
            }
        } else {
            System.out.println(res.getMessage());
        }

    }
}
