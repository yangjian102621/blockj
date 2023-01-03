package org.rockyang.blockj.client.cmd;

import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.cmd.utils.Printer;
import org.rockyang.blockj.client.rpc.BlockService;

import java.util.Arrays;
import java.util.HashMap;
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
	protected String version = "1.0.0";
	protected Map<String, Command> subCommands = new HashMap<>(8);
	protected BlockService blockService = null;

	public void init(String preUsage, String[] args)
	{
		if (args.length == 0) {
			return;
		}

		if (!subCommands.containsKey(args[0])) {
			showHelp(preUsage);
			return;
		}

		// call the command
		Command cmd = subCommands.get(args[0]);
		if (cmd.subCommands.size() == 0) {
			String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
			CliContext context = new CliContext(subArgs);
			if (context.getOption("help") != null) {
				cmd.showHelp(preUsage);
				return;
			}

			if (context.getOption("version") != null) {
				System.out.println(cmd.version);
				return;
			}

			cmd.action(context);
			return;
		}

		// go into the subcommands
		String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
		preUsage = String.format("%s %s", preUsage, cmd.getName());
		cmd.init(preUsage, subArgs);
	}

	abstract public void action(CliContext context);

	public void showHelp(String usagePrefix)
	{
		System.out.println("NAME:");
		Printer.printTabLine("%s %s - %s\n\n", usagePrefix, getName(), usage);
		System.out.println("USAGE:");
		Printer.printTabLine("%s %s command [command options] [arguments...]\n\n", usagePrefix, getName());

		if (subCommands.size() > 0) {
			System.out.println("COMMANDS:");
			subCommands.forEach((key, cmd) -> Printer.printTabLine("%-10s %s\n", key, cmd.getUsage()));
			System.out.println();
		}

		System.out.println("OPTIONS:");
		Printer.printTabLine("%-10s %s\n", "--api", "blockchain backend api url (default: 127.0.0.1:2345)");
		Printer.printTabLine("%-10s %s\n", "--help", "show help (default: false)");
		Printer.printTabLine("%-10s %s\n", "--version", "print the version (default: false)");

	}

	public String getName()
	{
		return name;
	}

	public String getUsage()
	{
		return usage;
	}

	public void addCommand(Command command)
	{
		this.subCommands.put(command.getName(), command);
	}
}
