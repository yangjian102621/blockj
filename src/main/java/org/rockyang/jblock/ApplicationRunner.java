package org.rockyang.jblock;

import org.rockyang.jblock.chain.Account;
import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Wallet;
import org.rockyang.jblock.chain.service.AccountService;
import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.chain.service.WalletService;
import org.rockyang.jblock.conf.MinerConfig;
import org.rockyang.jblock.miner.Miner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

	static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

	private final WalletService walletService;
	private final AccountService accountService;
	private final ChainService chainService;
	private final MinerConfig minerConfig;
	private final Miner miner;

	public ApplicationRunner(WalletService walletService,
	                         AccountService accountService,
	                         ChainService chainService,
	                         MinerConfig minerConfig,
	                         Miner miner)
	{
		this.walletService = walletService;
		this.accountService = accountService;
		this.chainService = chainService;
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
			walletService.setMinerWallet(wallet);
			// create the genesis account
			Account account = new Account(wallet.getAddress(), Miner.GENESIS_ACCOUNT_BALANCE, wallet.getPubKey(), 0);
			accountService.setAccount(account);
			logger.info("Initialize miner successfully, miner address: {}", wallet.getAddress());
			// generate genesis block
			Block block = miner.createGenesisBlock();
			chainService.addBlock(block);
			chainService.setChainHead(block.getHeader().getHeight());
			System.exit(0);
		}

	}

}
