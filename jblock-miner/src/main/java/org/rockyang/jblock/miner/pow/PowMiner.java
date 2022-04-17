package org.rockyang.jblock.miner.pow;

import org.rockyang.jblock.base.crypto.Sign;
import org.rockyang.jblock.base.model.*;
import org.rockyang.jblock.chain.service.AccountService;
import org.rockyang.jblock.chain.service.WalletService;
import org.rockyang.jblock.miner.Miner;
import org.springframework.stereotype.Component;

/**
 * The PoW algorithm implements
 *
 * @author yangjian
 */
@Component(value = "powMiner")
public class PowMiner implements Miner {

	// the genesis block nonce value
	public static final Long GENESIS_BLOCK_NONCE = 100000L;
	private final WalletService walletService;
	private final AccountService accountService;

	public PowMiner(WalletService walletService, AccountService accountService)
	{
		this.walletService = walletService;
		this.accountService = accountService;
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
		if (System.currentTimeMillis() - preBlockHeader.getTimestamp() <= Miner.BLOCK_DELAY_SECS * 1000L) {
			return null;
		}
		BlockHeader newBlockHeader = new BlockHeader(preBlockHeader.getHeight() + 1, preBlockHeader.getHash());
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
		message.setPubKey(minerKey.getPubKey());
		message.setValue(Miner.MINING_REWARD);

		// sign the message
		String sign = Sign.sign(minerKey.getPriKey(), message.toSigned());
		message.setSign(sign);
		newBlock.addMessage(message);

		// sign the block
		newBlock.setPubKey(minerKey.getPubKey());
		String blockSig = Sign.sign(minerKey.getPriKey(), newBlock.genCid());
		newBlock.setBlockSign(blockSig);

		return newBlock;
	}

	// create the genesis block
	public Block createGenesisBlock() throws Exception
	{
		// create the default wallet
		Wallet wallet = new Wallet();
		walletService.setMinerWallet(wallet);
		// init the reward address balance
		Account rewardAccount = new Account(Miner.REWARD_ADDR, Miner.TOTAL_SUPPLY, null, 0);
		accountService.setAccount(rewardAccount);

		// create the genesis message
		Message message = new Message();
		message.setFrom(Miner.REWARD_ADDR);
		message.setTo(wallet.getAddress());
		message.setParams("Miner Reward.");
		message.setCid(message.genMsgCid());
		message.setPubKey(wallet.getPubKey());
		message.setValue(Miner.GENESIS_ACCOUNT_BALANCE);
		// sign the message
		String sign = Sign.sign(wallet.getPriKey(), message.toSigned());
		message.setSign(sign);

		BlockHeader header = new BlockHeader(0, null);
		header.setNonce(PowMiner.GENESIS_BLOCK_NONCE);
		header.setDifficulty(ProofOfWork.getTarget());
		header.setHash(header.genHash());

		Block block = new Block(header);
		block.addMessage(message);

		// sign the block
		block.setPubKey(wallet.getPubKey());
		String blockSig = Sign.sign(wallet.getPriKey(), block.genCid());
		block.setBlockSign(blockSig);

		return block;
	}

	@Override
	public boolean validateBlock(Block block)
	{
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block.getHeader());
		return proofOfWork.validate();
	}
}
