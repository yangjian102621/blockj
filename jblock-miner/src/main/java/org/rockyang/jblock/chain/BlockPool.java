package org.rockyang.jblock.chain;

import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.base.utils.ThreadUtils;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * blocks pool
 * <p>
 * when mined a new block, push it to the block pool,
 * after it confirmed then move it to the chain store.
 *
 * @author yangjian
 */
@Component
public class BlockPool {

	private final static Logger logger = LoggerFactory.getLogger(BlockPool.class);
	private final Map<String, Block> blocks = new ConcurrentHashMap<>();
	private final AtomicLong head = new AtomicLong();
	private final BlockService blockService;

	public BlockPool(BlockService blockService)
	{
		this.blockService = blockService;
	}

	@PostConstruct
	public void run()
	{
		new Thread(() -> {
			while (true) {
				long head = blockService.chainHead();
				Block headBlock = blockService.getBlockByHeight(head);
				for (Block block : getBlocks()) {
					if (headBlock == null) {
						logger.info("Can not find the head block");
						break;
					}
					long diff = block.getHeader().getHeight() - head;
					long now = System.currentTimeMillis() / 1000;
					logger.debug("diff: {}, headTimestamp: {}", diff, headBlock.getHeader().getTimestamp());
					if (now < headBlock.getHeader().getTimestamp() + diff * Miner.BLOCK_DELAY_SECS) {
						continue;
					}
					block.getHeader().setTimestamp(headBlock.getHeader().getTimestamp() + diff * Miner.BLOCK_DELAY_SECS);
					try {
						Result result = blockService.checkBlock(block);
						if (result.isOk()) {
							blockService.markBlockAsValidated(block);
						} else {
							logger.warn("Invalid block, {}", result.getMessage());
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						removeBlock(block.getHeader().getHash());
					}
				}
				ThreadUtils.niceSleep(1);
			}
		}).start();
	}

	public void putBlock(Block block)
	{
		blocks.put(block.getHeader().getHash(), block);
		while (true) {
			long h = head.get();
			if (block.getHeader().getHeight() > h) {
				if (head.compareAndSet(h, block.getHeader().getHeight())) {
					break;
				}
			}
		}
	}

	public Block getBlock(String blockHash)
	{
		return blocks.get(blockHash);
	}

	public List<Block> getBlocks()
	{
		return new ArrayList<>(blocks.values());
	}

	public long getHead()
	{
		return head.get();
	}

	// remove message from the message pool
	public void removeBlock(String blockHash)
	{
		blocks.remove(blockHash);
	}

}
