package org.rockyang.jblock.client;

import org.rockyang.jblock.client.cmd.Chain;
import org.rockyang.jblock.client.cmd.Command;

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
		commands.put("chain", Chain.getInstance());

		if (args.length == 0) {
			// @TODO: print command help here
		}

		if (commands.get(args[0]) == null) {
			// @TODO: print command help here
		}

		// get the last arg
		for (String cmd : args) {

		}
	}

}
