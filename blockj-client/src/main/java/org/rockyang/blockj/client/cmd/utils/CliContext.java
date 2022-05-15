package org.rockyang.blockj.client.cmd.utils;

import org.rockyang.blockj.base.utils.CmdArgsParser;

/**
 * @author yangjian
 */
public class CliContext {
	private String[] args;
	private CmdArgsParser parser;

	public CliContext(String[] args)
	{
		this.args = args;
		this.parser = CmdArgsParser.getInstance(args);
	}

	public String[] getArgs()
	{
		return args;
	}

	public void setArgs(String[] args)
	{
		this.args = args;
	}

	public String getArg(int index)
	{
		return parser.getArgs().get(index);
	}

	public String getOption(String key)
	{
		return parser.getOption(key);
	}

	public String getOption(String key, String defaultValue)
	{
		return parser.getOption(key, defaultValue);
	}

	public Integer getIntOption(String key)
	{
		return parser.getIntOption(key);
	}

	public Integer getIntOption(String key, Integer defaultValue)
	{
		return parser.getIntOption(key, defaultValue);
	}

	public Boolean getBoolOption(String key)
	{
		return parser.getBoolOption(key);
	}

	public Boolean getBoolOption(String key, boolean defaultValue)
	{
		return parser.getBoolOption(key, defaultValue);
	}
}
