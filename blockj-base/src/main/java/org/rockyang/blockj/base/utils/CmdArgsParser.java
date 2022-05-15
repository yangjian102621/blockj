package org.rockyang.blockj.base.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cli command arguments parser
 *
 * @author yangjian
 */
public class CmdArgsParser {

	private final List<String> args;
	private final Map<String, String> options;
	private static volatile CmdArgsParser instance;

	private CmdArgsParser(String[] args)
	{
		this.args = new ArrayList<>();
		this.options = new HashMap<>(16);
		parse(args);
	}

	public static synchronized CmdArgsParser getInstance(String[] args)
	{
		if (instance == null) {
			instance = new CmdArgsParser(args);
		}
		return instance;
	}

	public void parse(String[] args)
	{
		if (args.length == 0) {
			return;
		}
		for (String item : args) {
			if (StringUtils.isEmpty(item)) {
				continue;
			}
			if (item.indexOf('-') == -1) {
				this.args.add(item);
				continue;
			}
			String[] split = StringUtils.split(item, '=');
			if (split.length == 2) {
				options.put(optionKey(split[0]), split[1]);
			} else {
				options.put(optionKey(split[0]), "");
			}

		}
	}

	public String optionKey(String s)
	{
		int i = 0;
		while (i < s.length()) {
			if (s.charAt(i) != '-' && !Character.isWhitespace(s.charAt(i))) {
				break;
			}
			i++;
		}
		return s.substring(i);
	}

	public List<String> getArgs()
	{
		return args;
	}

	public String getOption(String key)
	{
		return options.get(key);
	}

	public String getOption(String key, String defaultValue)
	{
		return options.get(key) == null ? defaultValue : options.get(key);
	}

	public Integer getIntOption(String key)
	{
		String s = getOption(key);
		return Integer.parseInt(s);
	}

	public Integer getIntOption(String key, Integer defaultValue)
	{
		String s = getOption(key);
		if (s == null) {
			return defaultValue;
		}
		return Integer.parseInt(s);
	}

	public Boolean getBoolOption(String key)
	{
		String s = getOption(key);
		return Boolean.getBoolean(s);
	}

	public Boolean getBoolOption(String key, boolean defaultValue)
	{
		String s = getOption(key);
		if (s == null) {
			return defaultValue;
		}
		return Boolean.getBoolean(s);
	}
}
