package org.rockyang.jblock;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.conf.MinerConfig;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.utils.SerializeUtils;
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
	private final Miner miner;
	@Value("${genesis}")
	private String genesis;
	@Value("${jblock.repo}")
	private String repo;

	public ApplicationRunner(BlockService blockService,
	                         MinerConfig minerConfig,
	                         Miner miner)
	{
		this.blockService = blockService;
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

		if (args[0].equals("genesis")) {
			logger.info("Try to create a genesis miner in {}", minerConfig.getRepo());
			// generate genesis block
			Block block = miner.createGenesisBlock();
			blockService.markBlockAsValidated(block);
			blockService.setChainHead(block.getHeader().getHeight());
			logger.info("Initialize miner successfully, genesis block hash: {}", block.getHeader().genHash());
			// generate the genesis block file
			String genesisFile = System.getProperty("user.dir") + "/genesis.car";
			byte[] bytes = SerializeUtils.serialize(block);
			FileOutputStream fos = new FileOutputStream(genesisFile);
			fos.write(bytes);
			fos.close();
			logger.info("generated the genesis file: {} successfully.", genesisFile);
			System.exit(0);
		} else if (args[0].equals("init")) {
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
			if (blockService.checkBlock(block, null)) {
				blockService.markBlockAsValidated(block);
				logger.info("init miner successfully, repo: {}", repo);
			}
			System.exit(0);
		}
	}

}
