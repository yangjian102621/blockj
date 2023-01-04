package org.rockyang.blockj.client.cmd.net;

import org.rockyang.blockj.base.model.Peer;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.client.cmd.Command;
import org.rockyang.blockj.client.cmd.utils.CliContext;
import org.rockyang.blockj.client.rpc.BlockService;

import java.util.List;

/**
 * @author yangjian
 */
public class NetPeers extends Command
{

    public NetPeers(BlockService service)
    {
        this.name = "peers";
        this.usage = "Print peers";
        this.blockService = service;
    }

    @Override
    public void action(CliContext context)
    {
        JsonVo<List<Peer>> res = blockService.netPeers();
        if (res.isOK()) {
            List<Peer> peers = res.getData();
            for (Peer peer : peers) {
                System.out.println(peer);
            }
        } else {
            System.out.println(res.getMessage());
        }
    }
}
