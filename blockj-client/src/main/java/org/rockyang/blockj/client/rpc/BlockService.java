package org.rockyang.blockj.client.rpc;

import org.rockyang.blockj.base.model.Block;
import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Peer;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.base.vo.MnemonicWallet;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yangjian
 */
public interface BlockService
{

    JsonVo<Wallet> newWallet();

    JsonVo<MnemonicWallet> newMnemonicWallet(String password);

    JsonVo<List<Wallet>> walletList();

    JsonVo<BigDecimal> getBalance(String address);

    JsonVo<String> sendMessage(String from, String to, BigDecimal value, String param);

    JsonVo<Message> getMessage(String cid);

    JsonVo<Long> chainHead();

    JsonVo<Block> getBlock(Long height);

    JsonVo<List<Peer>> netPeers();

    JsonVo<String> netListen();

    JsonVo<String> netConnect(String peerAddress);
}
