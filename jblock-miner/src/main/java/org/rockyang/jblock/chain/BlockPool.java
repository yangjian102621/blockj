package org.rockyang.jblock.chain;

import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.base.utils.ThreadUtils;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.service.BlockService;
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
	private final Map<Long, Block> blocks = new ConcurrentHashMap<>();
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
				if (headBlock == null) {
					logger.info("Can not find the head block");
					ThreadUtils.niceSleep(1);
					continue;
				}
				for (Block block : getBlocks()) {
					long diff = block.getHeader().getHeight() - head;
					long now = System.currentTimeMillis() / 1000;
					if (now < headBlock.getHeader().getTimestamp() + diff * Miner.BLOCK_DELAY_SECS) {
						continue;
					}
					block.getHeader().setTimestamp(headBlock.getHeader().getTimestamp() + diff * Miner.BLOCK_DELAY_SECS);
					try {
						Result result = blockService.checkBlock(block);
						if (result.isOk()) {
							blockService.markBlockAsValidated(block);
						} else {
							logger.warn("Invalid block, height: {}, message: {}", block.getHeader().getHeight(), result.getMessage());
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						removeBlock(block.getHeader().getHeight());
					}
				}
			}
		}).start();
	}

	public void putBlock(Block block)
	{
		if (hasBlock(block)) {
			return;
		}
		
		blocks.put(block.getHeader().getHeight(), block);
		// update the chain head
		while (true) {
			long h = head.get();
			if (block.getHeader().getHeight() > h) {
				if (head.compareAndSet(h, block.getHeader().getHeight())) {
					break;
				}
			}
		}
	}

	public boolean hasBlock(Block block)
	{
		return blocks.containsKey(block.getHeader().getHeight());
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
	public void removeBlock(Long height)
	{
		blocks.remove(height);
	}

}
