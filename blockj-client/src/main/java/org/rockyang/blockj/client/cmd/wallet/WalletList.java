package org.rockyang.blockj.client.cmd.wallet;

import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

import java.util.List;

/**
 * @author yangjian
 */
public class WalletList extends Command {

	public WalletList(BlockService service)
	{
		this.name = "list";
		this.usage = "List wallet address";
		this.blockService = service;
	}

	@Override
	public void action(CliContext context)
	{
		List<Wallet> wallets = blockService.walletList();
		if (wallets.size() > 0) {
			System.out.printf("%-45s%-15s%-5s\n", "Address", "Balance", "Nonce");
		}
		for (Wallet wallet : wallets) {
			System.out.printf("%-45s%-15s%-5s\n", wallet.getAddress(), wallet.getBalance(), wallet.getMessageNonce());
		}
	}
}
