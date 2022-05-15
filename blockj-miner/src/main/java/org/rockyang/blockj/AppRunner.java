package org.rockyang.blockj;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.base.crypto.Sign;
import org.rockyang.blockj.base.model.*;
import org.rockyang.blockj.base.store.Datastore;
import org.rockyang.blockj.base.store.RocksDatastore;
import org.rockyang.blockj.base.utils.CmdArgsParser;
import org.rockyang.blockj.base.utils.SerializeUtils;
import org.rockyang.blockj.miner.Miner;
import org.rockyang.blockj.miner.pow.PowMiner;
import org.rockyang.blockj.miner.pow.ProofOfWork;
import org.rockyang.blockj.service.AccountService;
import org.rockyang.blockj.service.BlockService;
import org.rockyang.blockj.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class AppRunner {

	static final Logger logger = LoggerFactory.getLogger(AppRunner.class);
	private String repo;
	private final CmdArgsParser parser;
	private Datastore datastore;

	public AppRunner(String[] args)
	{
		parser = CmdArgsParser.getInstance(args);
		repo = parser.getOption("repo");
		if (StringUtils.isEmpty(repo)) {
			repo = System.getProperty("BLOCKJ_PATH");
			if (StringUtils.isEmpty(repo)) {
				throw new RuntimeException("You must pass the option '--repo'");
			}
		}
	}

	public boolean preRun() throws Exception
	{
		String opt = parser.getArgs().get(0);
		if (StringUtils.isBlank(opt)) {
			System.out.println("No operation input");
			return false;
		}
		Block block;
		File dir = new File(repo);
		switch (opt) {
			case "genesis":
				logger.info("Try to create a genesis miner in {}", repo);
				if (dir.exists()) {
					throw new RuntimeException(String.format("A miner repo is already initialized in '%s'", dir));
				}
				datastore = new RocksDatastore(repo);
				// generate genesis block
				block = createGenesisBlock();
				// initialize the timestamp of block
				long createTime = block.getHeader().getCreateTime();
				block.getHeader().setTimestamp((createTime - (createTime % Miner.BLOCK_DELAY_SECS)) + Miner.BLOCK_DELAY_SECS);

				// save block
				saveBlock(block);
				logger.info("Initialize miner successfully, genesis block hash: {}", block.getHeader().getHash());

				// generate the genesis block file
				String genesisFile = System.getProperty("user.dir") + "/genesis.car";
				byte[] bytes = SerializeUtils.serialize(block);
				FileOutputStream fos = new FileOutputStream(genesisFile);
				fos.write(bytes);
				fos.close();
				logger.info("Generated the genesis file: {} successfully.", genesisFile);

				// generate properties file
				genPropertiesFile(parser);
				break;

			case "init":
				if (dir.exists()) {
					throw new RuntimeException(String.format("A miner repo is already initialized in '%s'", dir));
				}
				datastore = new RocksDatastore(repo);
				String genesis = parser.getOption("genesis");
				if (StringUtils.isBlank(genesis)) {
					logger.error("must pass genesis file path");
					System.exit(0);
				}
				File file = new File(genesis);
				if (!file.exists()) {
					logger.error("file {} not exits", file);
					System.exit(0);
				}
				byte[] data = new byte[(int) file.length()];
				FileInputStream fis = new FileInputStream(genesis);
				int read = fis.read(data);
				if (read != file.length()) {
					logger.error("read file {} failed", file);
					System.exit(0);
				}
				fis.close();
				block = (Block) SerializeUtils.unSerialize(data);
				Wallet wallet = createMinerWallet();
				saveBlock(block);
				logger.info("Initialize miner successfully, repo: {}, miner: {}", repo, wallet.getAddress());

				// generate properties file
				genPropertiesFile(parser);

				break;

			case "run":
				return true;
		}
		return false;
	}

	private Wallet createMinerWallet() throws Exception
	{
		// create the default wallet
		Wallet wallet = new Wallet();
		datastore.put(WalletService.WALLET_PREFIX + wallet.getAddress(), wallet);
		datastore.put(WalletService.MINER_ADDR_KEY, wallet.getAddress());
		// init the reward address balance
		Account rewardAccount = new Account(Miner.REWARD_ADDR, Miner.TOTAL_SUPPLY, null, 0);
		datastore.put(AccountService.ACCOUNT_PREFIX + rewardAccount.getAddress(), rewardAccount);
		return wallet;
	}

	private Block createGenesisBlock() throws Exception
	{
		Wallet wallet = createMinerWallet();

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
		header.setHash(header.genCid());

		Block block = new Block(header);
		block.addMessage(message);

		// sign the block
		block.setPubKey(wallet.getPubKey());
		String blockSig = Sign.sign(wallet.getPriKey(), block.genCid());
		block.setBlockSign(blockSig);

		return block;
	}

	private void saveBlock(Block block)
	{
		// save the block
		datastore.put(BlockService.BLOCK_PREFIX + block.getHeader().getHash(), block);
		// add search index for block height
		datastore.put(BlockService.BLOCK_HEIGHT_PREFIX + block.getHeader().getHeight(), block.getHeader().getHash());

		// add index for messages in block
		block.getMessages().forEach(message -> datastore.put(BlockService.BLOCK_MESSAGE_PREFIX + message.getCid(), message));
		// update chain head
		datastore.put(BlockService.CHAIN_HEAD_KEY, block.getHeader().getHeight());
	}

	public void cleanRepo()
	{
		File file = new File(repo);
		file.delete();
	}

	private void genPropertiesFile(CmdArgsParser parser) throws IOException
	{
		Properties properties = new Properties();
		properties.setProperty("server.address", parser.getOption("api.addr", "127.0.0.1"));
		properties.setProperty("server.port", parser.getOption("api.port", "8001"));
		properties.setProperty("blockj.repo", repo);
		properties.setProperty("blockj.enable-mining", parser.getOption("enable-mining", "false"));
		properties.setProperty("p2p.address", parser.getOption("p2p.addr", "127.0.0.1"));
		properties.setProperty("p2p.port", parser.getOption("p2p.port", "2345"));

		// load common properties
		properties.setProperty("genesis.address", "127.0.0.1");
		properties.setProperty("genesis.port", "2345");

		// disable tio logs
		properties.setProperty("logging.level.org.tio.server", "off");
		properties.setProperty("logging.level.org.tio.client", "off");

		properties.store(new FileWriter(repo + "/node.properties"), "Node config file");

	}

}
