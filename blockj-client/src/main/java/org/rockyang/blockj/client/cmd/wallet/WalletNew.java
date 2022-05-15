package org.rockyang.blockj.client.cmd.wallet;

import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;

/**
 * @author yangjian
 */
public class WalletNew extends Command {

	public WalletNew()
	{
		this.name = "new";
		this.usage = "Generate a new key";
	}

	@Override
	public void action(CliContext context)
	{

	}
}
