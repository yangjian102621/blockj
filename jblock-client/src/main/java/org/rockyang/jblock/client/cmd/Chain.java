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
		context.getArgs().forEach(System.out::println);
		System.out.printf("address: %s\n", context.getOption("address"));
	}

	@Override
	public void showHelp()
	{
		System.out.printf("xxxxx %d", 100);
	}

	public static Chain getInstance(String name)
	{
		synchronized (Chain.class) {
			if (instance == null) {
				instance = new Chain();
				instance.setName(name);
			}

			return instance;
		}
	}
}
