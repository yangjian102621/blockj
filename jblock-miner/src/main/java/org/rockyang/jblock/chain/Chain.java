package org.rockyang.jblock.chain;

import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.base.model.BlockHeader;
import org.rockyang.jblock.base.model.Message;
import org.rockyang.jblock.base.utils.ThreadUtils;
import org.rockyang.jblock.chain.event.NewBlockEvent;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
	@Value("${run-mining}")
	private boolean isRunMining;

	public Chain(Miner miner, MessagePool messagePool, BlockPool blockPool, BlockService blockService)
	{
		this.miner = miner;
		this.messagePool = messagePool;
		this.blockPool = blockPool;
		this.blockService = blockService;
	}

	@PostConstruct
	public void run()
	{
		new Thread(() -> {
			if (!isRunMining) {
				return;
			}
			logger.info("JBlock Miner started");
			while (true) {
				// get the chain head
				long head = blockPool.getHead();
				if (head <= 0) {
					long chainHead = blockService.chainHead();
					if (chainHead < 0) {
						logger.info("This miner is not initialized, initialize it with 'jblock init' command");
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

					logger.info("Mined a new block, Height: {}, Hash: {}", block.getHeader().getHeight(), block.getHeader().getHash());
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