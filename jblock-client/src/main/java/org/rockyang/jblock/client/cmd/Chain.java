package org.rockyang.jblock.client.cmd;

/**
 * @author yangjian
 */
public class Chain extends Command {

	private static volatile Chain instance;

	@Override
	public void action(CliContext context)
	{
		System.out.println(this.name);
	}

	public static Chain getInstance()
	{
		synchronized (Chain.class) {
			if (instance == null) {
				instance = new Chain();
			}

			return instance;
		}
	}
}
