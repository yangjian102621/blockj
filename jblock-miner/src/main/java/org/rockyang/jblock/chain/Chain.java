package org.rockyang.jblock.chain;

import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.base.model.BlockHeader;
import org.rockyang.jblock.base.model.Message;
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
import java.util.concurrent.atomic.AtomicLong;

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
	private final AtomicLong head;

	public Chain(Miner miner,
	             MessagePool messagePool,
	             BlockPool blockPool,
	             BlockService blockService)
	{
		this.miner = miner;
		this.messagePool = messagePool;
		this.blockPool = blockPool;
		this.blockService = blockService;
		head = new AtomicLong();
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
				if (head.get() <= 0) {
					long chainHead = blockService.chainHead();
					head.getAndSet(chainHead);
				}

				if (head.get() < 0) {
					niceSleep(3);
					continue;
				}
				// @TODO: fill the blocks of null round, ONLY the genesis miner allow to do this.

				Block preBlock = blockService.getBlockByHeight(head.get());
				if (preBlock == null) {
					niceSleep(3);
					continue;
				}
				BlockHeader preBlockHeader = preBlock.getHeader();
				// check if it's the time for a new round
				long now = System.currentTimeMillis() / 1000;
				if (now - preBlockHeader.getTimestamp() <= Miner.BLOCK_DELAY_SECS) {
					niceSleep(3);
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
					// put block to block pool
					blockPool.putBlock(block);
					head.getAndSet(block.getHeader().getHeight());
					// broadcast the block
					ApplicationContextProvider.publishEvent(new NewBlockEvent(block));

				} catch (Exception e) {
					niceSleep(3);
					e.printStackTrace();
				}

			}
		}).start();
	}

	public void niceSleep(int secs)
	{
		try {
			Thread.sleep(secs * 1000L);
		} catch (InterruptedException e) {
			logger.warn("received interrupt while trying to sleep in mining cycle");
		}
	}
}
