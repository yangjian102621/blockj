package org.rockyang.blockj.client.cmd;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.client.BlockClient;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.cmd.utils.Printer;
import org.rockyang.blockj.client.rpc.BlockService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Command line abstract class
 *
 * @author yangjian
 */
public abstract class Command
{

    // command
    protected String name;
    protected String fullName;
    // command description
    protected String usage;
    // command usage
    protected String argsUsage;
    // command options
    protected Map<String, String> options = new HashMap<>();
    protected Map<String, Command> subCommands = new HashMap<>(8);
    protected BlockService blockService = null;

    public void init(String[] args)
    {
        if (args.length == 0) {
            return;
        }
        if (!subCommands.containsKey(args[0])) {
            showHelp();
            return;
        }

        // call the command
        Command cmd = subCommands.get(args[0]);
        if (cmd.subCommands.size() == 0) {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            CliContext context = new CliContext(subArgs);
            if (context.getOption("help") != null) {
                cmd.showHelp();
                return;
            }

            if (context.getOption("version") != null) {
                System.out.println(BlockClient.VERSION);
                return;
            }

            cmd.action(context);
            return;
        }

        // go into the subcommands
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        cmd.init(subArgs);
    }

    abstract public void action(CliContext context);

    public void showHelp()
    {
        System.out.println("NAME:");
        Printer.printTabLine("%s%s - %s\n\n", BlockClient.ROOT_CMD, fullName, usage);
        System.out.println("USAGE:");
        if (StringUtils.isBlank(argsUsage)) {
            Printer.printTabLine("%s%s [command options] [arguments...]\n\n", BlockClient.ROOT_CMD, fullName);
        } else {
            Printer.printTabLine("%s%s %s\n\n", BlockClient.ROOT_CMD, fullName, argsUsage);
        }

        if (subCommands.size() > 0) {
            System.out.println("COMMANDS:");
            subCommands.forEach((key, cmd) -> Printer.printTabLine("%-10s %s\n", key, cmd.getUsage()));
            System.out.println();
        }

        if (options.size() > 0) {
            System.out.println("OPTIONS:");
            for (Map.Entry<String, String> entry : options.entrySet()) {
                Printer.printTabLine("%-10s %s\n", entry.getKey(), entry.getValue());
            }
        }


    }

    public String getName()
    {
        return name;
    }

    public String getArgsUsage()
    {
        return argsUsage;
    }

    public String getUsage()
    {
        return usage;
    }

    public void addCommand(Command command)
    {
        this.subCommands.put(command.getName(), command);
    }
}
