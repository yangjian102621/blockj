package org.rockyang.blockj.client.cmd;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public class Send extends Command
{

    public Send(BlockService service)
    {
        this.name = "send";
        this.fullName = "send";
        this.usage = "Send funds between accounts";
        this.argsUsage = "[targetAddress] [amount]";
        // 添加选项说明
        this.options.put("--from", "optionally specify the account to send funds from");
        this.options.put("--param", "specify invocation parameters");
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        String from = context.getOption("from");
        String to = context.getArg(0);
        BigDecimal value = context.getBigDecimalArg(1);
        String param = context.getOption("param");

        if (StringUtils.isBlank(to)) {
            System.out.println("Must pass to address");
            return;
        }

        if (value == null || value.equals(BigDecimal.ZERO)) {
            System.out.println("Invalid send value");
            return;
        }

        String cid = blockService.sendMessage(from, to, value, param);
        System.out.printf("Send message, CID: %s\n", cid);
    }
}
