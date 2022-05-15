package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.cmd.utils.Flag;
import org.rockyang.blockj.client.cmd.utils.Printer;
import org.rockyang.blockj.client.rpc.BlockjService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author yangjian
 * <p>
 * NAME:
 * blockj wallet - Manage wallet
 * <p>
 * USAGE:
 * blockj wallet command [command options] [arguments...]
 * <p>
 * COMMANDS:
 * new          Generate a new key of the given type
 * list         List wallet address
 * balance      Get account balance
 * export       export keys
 * import       import keys
 * default      Get default wallet address
 * set-default  Set default wallet address
 * delete       Delete an account from the wallet
 * help, h      Shows a list of commands or help for one command
 * <p>
 * OPTIONS:
 * --help, -h     show help (default: false)
 * --version, -v  print the version (default: false)
 */
public abstract class Command {

	protected String name;
	protected String usage;
	protected String argsUsage;
	protected List<Flag> flags;
	protected Map<String, Command> subCommands;
	protected BlockjService blockService;

	abstract public void action(CliContext context);

	public void showHelp(String usagePrefix)
	{
		System.out.println("NAME:");
		Printer.printTabLine("%s - %s\n", usagePrefix, usage);
	}

	public void init(String preUsage, String[] args)
	{
		if (!subCommands.containsKey(args[0])) {
			showHelp(preUsage);
			return;
		}


		String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
		CliContext context = new CliContext(subArgs);
		this.action(context);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUsage()
	{
		return usage;
	}

	public void setUsage(String usage)
	{
		this.usage = usage;
	}

	public String getArgsUsage()
	{
		return argsUsage;
	}

	public void setArgsUsage(String argsUsage)
	{
		this.argsUsage = argsUsage;
	}

	public List<Flag> getFlags()
	{
		return flags;
	}

	public void setFlags(List<Flag> flags)
	{
		this.flags = flags;
	}

	public void addCommand(Command command)
	{
		this.subCommands.put(command.getName(), command);
	}
}
