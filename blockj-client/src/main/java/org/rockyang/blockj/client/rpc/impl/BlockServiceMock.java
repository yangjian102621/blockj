package org.rockyang.blockj.client.rpc.impl;

import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Peer;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.client.rpc.BlockService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * mock implements for blockjService
 *
 * @author yangjian
 */
public class BlockServiceMock implements BlockService
{
    @Override
    public JsonVo<Wallet> newWallet()
    {
        return new JsonVo<>(JsonVo.SUCCESS, new Wallet("t16oicympjfdwwtkzwve6pwgigdw3xbz76k4t66va", BigDecimal.ZERO));
    }

    @Override
    public JsonVo<List<Wallet>> walletList()
    {
        List<Wallet> list = new ArrayList<>(3);
        list.add(new Wallet("t16oicympjfdwwtkzwve6pwgigdw3xbz76k4t66va", new BigDecimal("33124.6542")));
        list.add(new Wallet("t1arfcunbc767a56rvvalmbcdzooc4blqgukctffq", new BigDecimal("1000.3245")));
        list.add(new Wallet("t1wbh64v24vd6gjtwgu5vpybuvzukx4hoxdmmtzmy", new BigDecimal("8675.0976")));
        return new JsonVo<>(JsonVo.SUCCESS, list);
    }

    @Override
    public JsonVo<BigDecimal> getBalance(String address)
    {
        return new JsonVo<>(JsonVo.SUCCESS, BigDecimal.ZERO);
    }

    @Override
    public JsonVo<String> sendMessage(String from, String to, BigDecimal value, String param)
    {
        return new JsonVo<>(JsonVo.SUCCESS, new Message().getCid());
    }

    @Override
    public JsonVo<Message> getMessage(String cid)
    {
        return new JsonVo<>(JsonVo.SUCCESS, new Message());
    }

    @Override
    public JsonVo<Long> chainHead()
    {
        return new JsonVo<>(JsonVo.SUCCESS, Long.valueOf("12345678"));
    }

    @Override
    public JsonVo<List<Peer>> netPeers()
    {
        return null;
    }

    @Override
    public JsonVo<String> netListen()
    {
        return null;
    }

    @Override
    public JsonVo<String> netConnect(String peerAddress)
    {
        return null;
    }
}
