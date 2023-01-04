package org.rockyang.blockj.client;

import org.rockyang.blockj.base.utils.CmdArgsParser;
import org.rockyang.blockj.client.cmd.Chain;
import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.Net;
import org.rockyang.blockj.client.cmd.Wallet;
import org.rockyang.blockj.client.cmd.utils.Printer;
import org.rockyang.blockj.client.rpc.BlockService;
import org.rockyang.blockj.client.rpc.impl.BlockServiceImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * client application main class
 *
 * @author yangjian
 */
public class BlockClient
{

    private static final Map<String, Command> commands = new HashMap<>(16);
    public static final String VERSION = "1.0.0";
    public static final String ROOT_CMD = "./client ";

    public static void main(String[] argsOri)
    {
        // init BlockService
        CmdArgsParser parser = CmdArgsParser.getInstance(argsOri);
        String api = parser.getOption("api", "http://127.0.0.1:8001");
        Boolean debug = parser.getBoolOption("debug", false);
//		BlockServiceMock blockService = new BlockServiceMock();
        BlockService blockService = new BlockServiceImpl(api, debug);
        addCommand(new Chain(blockService));
        addCommand(new Wallet(blockService));
        addCommand(new Net(blockService));
        String[] args = parser.getArgs().toArray(new String[0]);

        if (args.length == 0 || !commands.containsKey(args[0])) {
            showHelp();
            return;
        }

        // extract args
        int index = 0;
        for (int i = 0; i < args.length; i++) {
            if (!commands.containsKey(args[i])) {
                index = i;
                break;
            }
        }

        String[] subArgs = Arrays.copyOfRange(args, index, args.length);
//		Arrays.stream(preArgs).forEach(System.out::println);
        commands.get(args[0]).init(subArgs);
    }

    private static void showHelp()
    {
        System.out.println("NAME:");
        Printer.printTabLine("%s\n\n", "./client - blockchain network client");
        System.out.println("USAGE:");
        Printer.printTabLine("%s\n\n", "./client command [command options] [arguments...]");
        System.out.println("VERSION:");
        Printer.printTabLine("%s\n\n", VERSION);
        System.out.println("COMMANDS:");
        commands.forEach((key, command) -> Printer.printTabLine("%-10s  %s\n", command.getName(), command.getUsage()));
        System.out.println();
        System.out.println("OPTIONS:");
        Printer.printTabLine("%-10s %s\n", "--api", "blockchain backend api url (default: http://127.0.0.1:8001)");
        Printer.printTabLine("%-10s %s\n", "--version", "print the version (default: false)");
        Printer.printTabLine("%-10s %s\n", "--help", "show help (default: false)");
    }

    public static void addCommand(Command command)
    {
        commands.put(command.getName(), command);
    }

}
