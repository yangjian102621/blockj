package org.rockyang.jblock.client;

import org.apache.commons.cli.*;

/**
 * jblock client main class
 * @author yangjian
 */
public class JBlockClient {

	public static void main(String args[]) throws ParseException
	{
		final Options options = new Options();
		final Option option = new Option("file", true, "Configuration file path");
		options.addOption(option);

		final CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		System.out.println("Your first argument is: " + cmd.getOptionValue("file"));
	}

}
