package org.rockyang.blockj.client;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.client.cmd.Chain;
import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.Send;
import org.rockyang.blockj.client.cmd.Wallet;
import org.rockyang.blockj.client.cmd.utils.Printer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * blockj client main class
 *
 * @author yangjian
 */
public class BlockjClient {

	private static Map<String, Command> commands = new HashMap(16);
	private static final String ROOT_CMD = "client ";

	public static void main(String args[])
	{
		addCommand(new Chain());
		addCommand(new Wallet());
		addCommand(new Send());

		if (args.length == 0 || !commands.containsKey(args[0])) {
			showHelp();
			return;
		}

		// extract args
		int index = 0;
		for (int i = 0; i < args.length; i++) {
			if (!commands.containsKey(args[i])) {
				index = i;
				break;
			}
		}
		String[] preArgs = Arrays.copyOfRange(args, 0, index);
//		Arrays.stream(args).forEach(System.out::println);
		commands.get(args[0]).init(ROOT_CMD + StringUtils.joinWith(" ", preArgs), Arrays.copyOfRange(args, index, args.length));
	}

	private static void showHelp()
	{
		System.out.println("NAME:");
		Printer.printTabLine("%s\n\n", "client - blockchain network client");
		System.out.println("USAGE:");
		Printer.printTabLine("%s\n\n", "client command [command options] [arguments...]");
		System.out.println("VERSION:");
		Printer.printTabLine("%s\n\n", "1.0.0");
		System.out.println("COMMANDS:");
		commands.forEach((key, command) -> Printer.printTabLine("%-10s  %s\n", command.getName(), command.getUsage()));
		System.out.println();
		System.out.println("OPTIONS:");
		Printer.printTabLine("%-10s %s\n", "--version", "print the version (default: false)");
		Printer.printTabLine("%-10s %s\n", "--help", "show help (default: false)");
	}

	public static void addCommand(Command command)
	{
		commands.put(command.getName(), command);
	}

}
