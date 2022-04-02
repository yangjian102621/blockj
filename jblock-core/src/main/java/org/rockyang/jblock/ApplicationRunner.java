package org.rockyang.jblock;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.conf.MinerConfig;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author yangjian
 */
@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

	static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

	private final ChainService chainService;
	private final MinerConfig minerConfig;
	private final Miner miner;

	public ApplicationRunner(ChainService chainService,
	                         MinerConfig minerConfig,
	                         Miner miner)
	{
		this.chainService = chainService;
		this.minerConfig = minerConfig;
		this.miner = miner;
	}

	@Override
	public void run(ApplicationArguments arguments) throws Exception
	{
		String[] args = arguments.getSourceArgs();
		if (args.length > 0 && args[0].equals("genesis")) {
			logger.info("Try to create a genesis miner in {}", minerConfig.getRepo());
			// generate genesis block
			Block block = miner.createGenesisBlock();
			chainService.saveBlock(block);
			chainService.setChainHead(block.getHeader().getHeight());
			logger.info("Initialize miner successfully, genesis block hash: {}", block.genCid());
			// generate the genesis block file
			String genesisFile = System.getProperty("user.dir")+"/genesis.car";
			byte[] bytes = SerializeUtils.serialize(block);
			FileOutputStream fos = new FileOutputStream(genesisFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(bytes);
			oos.flush();
			fos.close();
			logger.info("generated the genesis file: {} successfully.", genesisFile);
			System.exit(0);
		}
	}

}
