package org.rockyang.jblock.miner.pow;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.BlockHeader;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.Wallet;
import org.rockyang.jblock.crypto.Sign;
import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.miner.Miner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * The PoW algorithm implements
 * @author yangjian
 */
@Component
public class PowMiner implements Miner {

	private final static Logger logger = LoggerFactory.getLogger(PowMiner.class);
	// the genesis block nonce value
	public static final Long GENESIS_BLOCK_NONCE = 100000L;
	private final Datastore datastore;

	public PowMiner(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@PostConstruct
	public void run()
	{
		new Thread(() -> {
			while (true) {
				// get the last block
				String chainHead = datastore.chainHead();
				if (chainHead == null ) {
					throw new RuntimeException("No chain head found");
				}
				logger.info("chainHead {}", chainHead);
				Block preBlock = datastore.getBlock(chainHead);
				if (preBlock == null) {
					throw new RuntimeException("No base block found");
				}
				BlockHeader preBlockHeader = preBlock.getHeader();
				// check if it's the time for new round
				if (System.currentTimeMillis() - preBlockHeader.getTimestamp() <= Miner.BLOCK_DELAY_SECS*1000L) {
					niceSleep(5);
					continue;
				}
				final ExecutorService executor = Executors.newFixedThreadPool(1);
				Callable<Block> callable = () -> mineOne(preBlock);
				Future<Block> future = executor.submit(callable);
				try {
					future.get(Miner.BLOCK_DELAY_SECS, TimeUnit.SECONDS);
					Block block = callable.call();
					logger.info("Mined a new block: {}", block);

					// @TODO: broadcast the block
					// @TODO: package the messages in the message pool
				} catch (Exception e) {
					logger.warn("Failed to mined a block {}", e.toString());
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

	@Override
	public Block mineOne(Block preBlock) throws Exception
	{
		// fetch the miner key
		Optional<Object> minerAddr = datastore.get(Miner.MINER_ADDR_KEY);
		if (minerAddr.isEmpty()) {
			throw new RuntimeException("No miner address set.");
		}
		Wallet minerKey = datastore.getWallet(minerAddr.get().toString());
		if (minerKey == null) {
			throw new RuntimeException("No miner address set.");
		}
		BlockHeader preBlockHeader = preBlock.getHeader();
		// check if it's the time for new round
		if (System.currentTimeMillis() - preBlockHeader.getTimestamp() <= Miner.BLOCK_DELAY_SECS*1000L) {
			return null;
		}
		BlockHeader newBlockHeader = new BlockHeader(preBlockHeader.getHeight()+1, preBlockHeader.getHash());
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(newBlockHeader);
		// run the proof of work, and get the result
		PowResult result = proofOfWork.run();
		newBlockHeader.setDifficulty(result.getTarget());
		newBlockHeader.setNonce(result.getNonce());
		newBlockHeader.setHash(result.getHash());

		Block newBlock = new Block(newBlockHeader);
		// create a message for miner reward
		Message message = new Message();
		message.setFrom(Miner.REWARD_ADDR);
		message.setTo(minerKey.getAddress());
		message.setParams("Miner Reward.");
		message.setCid(message.getCid());
		message.setValue(Miner.MINING_REWARD);

		// sign the message
		String sign = Sign.sign(minerKey.getPriKey(), message.toSigned());
		message.setSign(sign);
		newBlock.addMessage(message);

		// sign the block
		String blockSig = Sign.sign(minerKey.getPriKey(), newBlock.genBlockHash());
		newBlock.setBlockSign(blockSig);

		return newBlock;
	}

	// create the genesis block
	public Block createGenesisBlock()
	{
		BlockHeader header = new BlockHeader(1, null);
		header.setNonce(PowMiner.GENESIS_BLOCK_NONCE);
		header.setDifficulty(ProofOfWork.getTarget());

		Block block = new Block(header);
		header.setHash(block.genBlockHash());
		return block;
	}

	@Override
	public boolean validateBlock(Block block) {
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block.getHeader());
		return proofOfWork.validate();
	}
}
