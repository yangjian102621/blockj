package org.rockyang.jblock.utils;

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
	private Map<String, String> options;

	public CmdArgsParser(String[] args)
	{
		this.args = new ArrayList<>();
		this.options = new HashMap<>(16);
		parse(args);
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
			if (item.indexOf('=') == -1) {
				this.args.add(item);
				continue;
			}
			String[] split = StringUtils.split(item, '=');
			options.put(optionKey(split[0]), split[1]);
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

	public String getArgs(int index)
	{
		if (index >= args.size()) {
			return null;
		}
		return args.get(index);
	}

	public String getOption(String key)
	{
		return options.get(key);
	}

	public Integer getIntOption(String key)
	{
		String s = getOption(key);
		return Integer.parseInt(s);
	}

	public Boolean getBoolOption(String key)
	{
		String s = getOption(key);
		return Boolean.getBoolean(s);
	}
}
