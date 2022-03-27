package org.rockyang.jblock.miner.pow;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.Wallet;
import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.miner.Miner;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * The PoW algorithm implements
 * @author yangjian
 */
@Component
public class PowMiner implements Miner {

	// the genesis block nonce value
	public static final Long GENESIS_BLOCK_NONCE = 100000L;
	private Datastore datastore;

	public PowMiner(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public Block mineOne(Optional<Block> preBlock) {

		// fetch the miner key
		Optional<Object> minerAddr = datastore.get(MINER_ADDR_KEY);
		if (minerAddr.isEmpty()) {
			throw new RuntimeException("No miner address set.");
		}
		Wallet minerKey = datastore.getWallet(minerAddr.get().toString());
		if (minerKey == null) {
			throw new RuntimeException("No miner address set.");
		}

		Block newBlock = preBlock.map(block -> new Block(block.getHeight(), block.getHash())).orElseGet(this::createGenesisBlock);
		// create a message for miner reward
		Message message = new Message();
		message.setFrom(Miner.REWARD_ADDR);
		message.setTo(minerKey.getAddress());
		message.setParams("Miner Reward.");
		message.setCid(message.getCid());
		message.setValue(Miner.MINING_REWARD);

		// run the proof of work, and get the result
		if (preBlock.isPresent()) {
			ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(newBlock);
			PowResult result = proofOfWork.run();
			newBlock.setDifficulty(result.getTarget());
			newBlock.setNonce(result.getNonce());
			newBlock.setHash(result.getHash());
		}
		newBlock.addMessage(message);

		return newBlock;
	}

	// create the genesis block
	private Block createGenesisBlock() {

		Block block = new Block();
		block.setNonce(PowMiner.GENESIS_BLOCK_NONCE);
		block.setDifficulty(ProofOfWork.getTarget());
		block.setHash(block.genBlockHash());
		return block;
	}

	@Override
	public boolean validateBlock(Block block) {
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
		return proofOfWork.validate();
	}
}
