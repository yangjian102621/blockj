package org.rockyang.jblock.miner.pow;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.BlockHeader;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.Wallet;
import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.chain.service.WalletService;
import org.rockyang.jblock.crypto.Sign;
import org.rockyang.jblock.miner.Miner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The PoW algorithm implements
 * @author yangjian
 */
@Component
public class PowMiner implements Miner {

	private final static Logger logger = LoggerFactory.getLogger(PowMiner.class);
	// the genesis block nonce value
	public static final Long GENESIS_BLOCK_NONCE = 100000L;
	private final ChainService chainService;
	private final WalletService walletService;

	public PowMiner(ChainService chainService, WalletService walletService)
	{
		this.chainService = chainService;
		this.walletService = walletService;
	}

	@PostConstruct
	public void run()
	{
		new Thread(() -> {
			while (true) {
				// get the chain head
				Object chainHead = chainService.chainHead();
				if (chainHead == null ) {
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
				if (System.currentTimeMillis() - preBlockHeader.getTimestamp() <= Miner.BLOCK_DELAY_SECS*1000L) {
					niceSleep(5);
					continue;
				}

				try {
					Block block = mineOne(preBlock);

					logger.info("Mined a new block, Height: {}, Cid: {}", block.getHeader().getHeight(),
							block.genCid());
					// @TODO: package the messages in the message pool
					// @TODO: broadcast the block
					chainService.addBlock(block);
					chainService.setChainHead(block.getHeader().getHeight());
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

	@Override
	public Block mineOne(Block preBlock) throws Exception
	{
		// fetch the miner key
		Wallet minerKey = walletService.getMinerWallet();
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
		message.setCid(message.genMsgCid());
		message.setValue(Miner.MINING_REWARD);

		// sign the message
		String sign = Sign.sign(minerKey.getPriKey(), message.toSigned());
		message.setSign(sign);
		newBlock.addMessage(message);

		// sign the block
		String blockSig = Sign.sign(minerKey.getPriKey(), newBlock.genCid());
		newBlock.setBlockSign(blockSig);

		return newBlock;
	}

	// create the genesis block
	public Block createGenesisBlock()
	{
		BlockHeader header = new BlockHeader(1, null);
		header.setNonce(PowMiner.GENESIS_BLOCK_NONCE);
		header.setDifficulty(ProofOfWork.getTarget());
		header.setHash(header.genHash());

		return new Block(header);
	}

	@Override
	public boolean validateBlock(Block block) {
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block.getHeader());
		return proofOfWork.validate();
	}
}
