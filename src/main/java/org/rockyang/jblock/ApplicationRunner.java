package org.rockyang.jblock;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Wallet;
import org.rockyang.jblock.conf.MinerConfig;
import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.miner.Miner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yangjian
 * @since 2019-06-03 下午5:18.
 */
@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {
	@Resource
	private ApplicationArguments arguments;

	static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

	private final Datastore datastore;
	private final MinerConfig minerConfig;
	private final Miner miner;

	public ApplicationRunner(Datastore datastore, MinerConfig minerConfig, Miner miner)
	{
		this.datastore = datastore;
		this.minerConfig = minerConfig;
		this.miner = miner;
	}

	@Override
	public void run(ApplicationArguments arguments) throws Exception
	{
		String[] args = arguments.getSourceArgs();
		if (args.length > 0 && args[0].equals("init")) {
			logger.info("Try to init the miner repo in {}", minerConfig.getRepo());
			// create the default wallet
			Wallet wallet = new Wallet();
			datastore.put(Miner.MINER_ADDR_KEY, wallet.getAddress());
			datastore.putWallet(wallet);
			logger.info("Initialize miner successfully, miner address: {}", wallet.getAddress());
			// generate genesis block
			Block block = miner.createGenesisBlock();
			datastore.putBlock(block);
			datastore.setChainHead(block.getHeader().getHash());
			System.exit(0);
		}

	}

}
