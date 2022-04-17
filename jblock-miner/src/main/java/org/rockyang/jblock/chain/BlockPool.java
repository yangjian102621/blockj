package org.rockyang.jblock.chain;

import org.rockyang.jblock.base.model.Block;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * blocks pool
 *
 * when mined a new block, push it to the block pool,
 * after it confirmed then move it to the chain store.
 *
 * @author yangjian
 */
@Component
public class BlockPool {

	private final Map<String, Block> blocks = new ConcurrentHashMap<>();

	public void putBlock(Block block)
	{
		blocks.put(block.getHeader().getHash(), block);
	}

	public Block getBlock(String blockHash)
	{
		return blocks.get(blockHash);
	}

	// remove message from the message pool
	public void removeBlock(String blockHash)
	{
		blocks.remove(blockHash);
	}

}
