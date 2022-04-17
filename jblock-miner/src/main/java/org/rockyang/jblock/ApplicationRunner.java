package org.rockyang.jblock;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.base.model.Account;
import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.base.model.Wallet;
import org.rockyang.jblock.base.utils.CmdArgsParser;
import org.rockyang.jblock.base.utils.SerializeUtils;
import org.rockyang.jblock.chain.service.AccountService;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.chain.service.WalletService;
import org.rockyang.jblock.conf.MinerConfig;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author yangjian
 */
@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

	static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

	private final BlockService blockService;
	private final MinerConfig minerConfig;
	private final WalletService walletService;
	private final AccountService accountService;
	private final Miner miner;
	@Value("${jblock.repo}")
	private String repo;

	public ApplicationRunner(BlockService blockService,
	                         WalletService walletService,
	                         AccountService accountService,
	                         MinerConfig minerConfig,
	                         Miner miner)
	{
		this.blockService = blockService;
		this.walletService = walletService;
		this.accountService = accountService;
		this.minerConfig = minerConfig;
		this.miner = miner;
	}

	@Override
	public void run(ApplicationArguments arguments) throws Exception
	{
		String[] args = arguments.getSourceArgs();
		if (args.length == 0) {
			return;
		}

		CmdArgsParser parser = new CmdArgsParser(args);
		if (StringUtils.equals(parser.getArgs(0), "genesis")) {
			logger.info("Try to create a genesis miner in {}", minerConfig.getRepo());
			// generate genesis block
			Block block = miner.createGenesisBlock();
			blockService.markBlockAsValidated(block);
			logger.info("Initialize miner successfully, genesis block hash: {}", block.getHeader().getHash());
			// update chain head
			blockService.setChainHead(block.getHeader().getHeight());
			// generate the genesis block file
			String genesisFile = System.getProperty("user.dir") + "/genesis.car";
			byte[] bytes = SerializeUtils.serialize(block);
			FileOutputStream fos = new FileOutputStream(genesisFile);
			fos.write(bytes);
			fos.close();
			logger.info("generated the genesis file: {} successfully.", genesisFile);
			System.exit(0);
		} else if (StringUtils.equals(parser.getArgs(0), "init")) {
			String genesis = parser.getOption("genesis");
			if (StringUtils.isEmpty(genesis)) {
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
			Block block = (Block) SerializeUtils.unSerialize(data);
			Result result = blockService.checkBlock(block);
			if (result.isOk()) {
				// create the default wallet
				Wallet wallet = new Wallet();
				walletService.setMinerWallet(wallet);
				// init the reward address balance
				Account rewardAccount = new Account(Miner.REWARD_ADDR, Miner.TOTAL_SUPPLY, null, 0);
				accountService.setAccount(rewardAccount);
				// validate genesis block
				blockService.markBlockAsValidated(block);
				logger.info("init miner successfully, repo: {}", repo);
			}
			System.exit(0);
		}
	}

}
