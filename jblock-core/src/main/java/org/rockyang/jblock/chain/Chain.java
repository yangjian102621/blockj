package org.rockyang.jblock.chain;

import org.rockyang.jblock.chain.event.NewBlockEvent;
import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.net.ApplicationContextProvider;
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
	private final ChainService chainService;

	public Chain(Miner miner,
	             MessagePool messagePool,
	             ChainService chainService)
	{
		this.miner = miner;
		this.messagePool = messagePool;
		this.chainService = chainService;
	}

	@PostConstruct
	public void run()
	{
		new Thread(() -> {
			while (true) {
				// get the chain head
				Object chainHead = chainService.chainHead();
				if (chainHead == null) {
					niceSleep(5);
					continue;
				}
				Block preBlock = chainService.getBlock(chainHead);
				if (preBlock == null) {
					niceSleep(5);
					continue;
				}
				BlockHeader preBlockHeader = preBlock.getHeader();
				// check if it's the time for a new round
				if (System.currentTimeMillis() - preBlockHeader.getTimestamp() <= Miner.BLOCK_DELAY_SECS * 1000L) {
					niceSleep(5);
					continue;
				}

				try {
					Block block = miner.mineOne(preBlock);

					logger.info("Mined a new block, Height: {}, Cid: {}", block.getHeader().getHeight(), block.genCid());
					// package the messages in block from message pool
					// @TODO: Should we limit the number of messages in each block?
					// @TODO: Should we sort the message by message nonce?
					Iterator<Message> iterator = messagePool.getMessages().iterator();
					while (iterator.hasNext()) {
						Message message = iterator.next();
						block.addMessage(message);
						// remove message from message pool
						iterator.remove();
					}

					// save block and execute messages in block
					chainService.saveBlock(block);

					// broadcast the block
					ApplicationContextProvider.publishEvent(new NewBlockEvent(block));

				} catch (Exception e) {
					niceSleep(5);
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
