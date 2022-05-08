package org.rockyang.jblock.client;

import org.rockyang.jblock.client.cmd.Chain;
import org.rockyang.jblock.client.cmd.Command;
import org.rockyang.jblock.client.cmd.utils.CliContext;
import org.rockyang.jblock.client.cmd.utils.Printer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * jblock client main class
 *
 * @author yangjian
 */
public class JBlockClient {

	private static Map<String, Command> commands = new HashMap(16);

	public static void main(String args[])
	{
		commands.put("chain", new Chain());
		if (args.length == 0) {
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

		if (index == 0) {
			showHelp();
			return;
		}

		String[] subArgs = Arrays.copyOfRange(args, index, args.length);
		String[] preArgs = Arrays.copyOfRange(args, 0, index);
		System.out.println(Arrays.toString(preArgs));
		CliContext context = new CliContext(subArgs);
		commands.get(args[index - 1]).action(context);
		showHelp();
	}

	private static void showHelp()
	{
		System.out.println("NAME:");
		Printer.printTabLine("%s\n", "jblock - JBlock network client");
		System.out.println();
		System.out.println("USAGE:");
		Printer.printTabLine("%s\n", "jblock command [command options] [arguments...]");
		System.out.println();
		System.out.println("VERSION:");
		Printer.printTabLine("%s\n", "1.0.0");
		System.out.println();
		System.out.println("COMMANDS:");
		commands.forEach((key, command) -> Printer.printTabLine("%-10s  %s\n", command.getName(), command.getUsage()));

		System.out.println();
		System.out.println("OPTIONS:");
		Printer.printTabLine("%-15s %s\n", "--version, -v", "print the version (default: false)");
		Printer.printTabLine("%-15s %s\n", "--help, -h", "show help (default: false)");
	}

}
