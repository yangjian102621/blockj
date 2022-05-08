package org.rockyang.jblock.client.cmd.utils;

import org.rockyang.jblock.base.utils.CmdArgsParser;

import java.util.List;

/**
 * @author yangjian
 */
public class CliContext {

	private CmdArgsParser parser;

	public CliContext(String[] args)
	{
		this.parser = CmdArgsParser.getInstance(args);
	}

	public List<String> getArgs()
	{
		return parser.getArgs();
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
