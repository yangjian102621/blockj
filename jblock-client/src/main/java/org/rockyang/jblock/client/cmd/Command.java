package org.rockyang.jblock.client.cmd;

import java.util.List;

/**
 * @author yangjian
 * <p>
 * NAME:
 * jblock wallet - Manage wallet
 * <p>
 * USAGE:
 * jblock wallet command [command options] [arguments...]
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
	protected List<Command> subCommands;

	abstract public void action(CliContext context);

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

	public List<Command> getSubCommands()
	{
		return subCommands;
	}

	public void setSubCommands(List<Command> subCommands)
	{
		this.subCommands = subCommands;
	}
}
