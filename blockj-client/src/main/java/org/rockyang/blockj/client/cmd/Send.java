package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.utils.CliContext;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public class Send extends Command {

	public Send()
	{
		this.name = "send";
		this.usage = "Send funds between accounts";
	}

	@Override
	public void action(CliContext context)
	{
		String from = context.getOption("from");
		String to = context.getOption("to");
		BigDecimal value = context.getBigDecimal("value");
		String param = context.getOption("param");

		String cid = blockService.sendMessage(from, to, value, param);
		System.out.printf("Send message, CID: %s\n", cid);
	}
}
