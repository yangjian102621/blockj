package org.rockyang.blockj.client.cmd.utils;

/**
 * @author yangjian
 */
public class Printer {

	public static void printTabLine(String format, Object... args)
	{
		System.out.printf("%2s", "");
		System.out.printf(format, args);
	}
}
