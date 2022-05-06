package org.rockyang.jblock.client;

import org.rockyang.jblock.client.cmd.Chain;
import org.rockyang.jblock.client.cmd.CliContext;
import org.rockyang.jblock.client.cmd.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * jblock client main class
 *
 * @author yangjian
 */
public class JBlockClient {

	public static void main(String args[])
	{
		Map<String, Command> commands = new HashMap(16);
		commands.put("chain", Chain.getInstance("chain"));

		if (args.length == 0) {
			// @TODO: print command help here
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
			// @TODO: print command help here
			return;
		}

		String[] subArgs = Arrays.copyOfRange(args, index, args.length);
		CliContext context = new CliContext(subArgs);
		commands.get(args[index - 1]).action(context);
	}

}
