package org.rockyang.blockj.chain;

import org.rockyang.blockj.base.model.Block;
import org.rockyang.blockj.base.model.BlockHeader;
import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.utils.ThreadUtils;
import org.rockyang.blockj.chain.event.NewBlockEvent;
import org.rockyang.blockj.service.BlockService;
import org.rockyang.blockj.conf.ApplicationContextProvider;
import org.rockyang.blockj.conf.MinerConfig;
import org.rockyang.blockj.miner.Miner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;

/**
 * @author yangjian
 */
@Component
public class Chain {

	private static final Logger logger = LoggerFactory.getLogger(Chain.class);

	@Qualifier(value = "powMiner")
	private final Miner miner;
	private final MessagePool messagePool;
	private final BlockPool blockPool;
	private final BlockService blockService;
	private final MinerConfig minerConfig;

	public Chain(Miner miner, MessagePool messagePool, BlockPool blockPool, BlockService blockService, MinerConfig minerConfig)
	{
		this.miner = miner;
		this.messagePool = messagePool;
		this.blockPool = blockPool;
		this.blockService = blockService;
		this.minerConfig = minerConfig;
	}

	@PostConstruct
	public void run()
	{
		new Thread(() -> {
			if (!minerConfig.isEnabledMining()) {
				return;
			}
			logger.info("blockj Miner started");
			while (true) {
				// get the chain head
				long head = blockPool.getHead();
				if (head <= 0) {
					long chainHead = blockService.chainHead();
					if (chainHead < 0) {
						logger.info("This miner is not initialized, initialize it with 'blockj init' command");
						return;
					}
					head = chainHead;
				}
				// @TODO: fill the blocks of null round, ONLY the genesis miner allow to do this.
				// Maybe it is a case?

				Block preBlock = blockService.getBlockByHeight(head);
				if (preBlock == null) {
					ThreadUtils.niceSleep(3);
					continue;
				}
				BlockHeader preBlockHeader = preBlock.getHeader();
				// check if it's the time for a new round
				long now = System.currentTimeMillis() / 1000;
				if (now - preBlockHeader.getTimestamp() <= Miner.BLOCK_DELAY_SECS) {
					ThreadUtils.niceSleep(3);
					continue;
				}

				try {
					Block block = miner.mineOne(preBlock);

					logger.info("Mined a new block, Height: {}", block.getHeader().getHeight());
					// package the messages in block from message pool
					// @TODO: Should we limit the number of messages in each block?
					// @TODO: Should we sort the message by message nonce?
					Iterator<Message> iterator = messagePool.getMessages().iterator();
					while (iterator.hasNext()) {
						Message message = iterator.next();
						block.addMessage(message);
						// remove from message pool
						iterator.remove();
					}
					// put block to pool
					blockPool.putBlock(block);
					// broadcast the block
					ApplicationContextProvider.publishEvent(new NewBlockEvent(block));

				} catch (Exception e) {
					ThreadUtils.niceSleep(3);
					e.printStackTrace();
				}

			}
		}).start();
	}

}
