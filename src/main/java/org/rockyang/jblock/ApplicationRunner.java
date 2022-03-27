package org.rockyang.jblock;

import org.rockyang.jblock.db.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 * @since 2019-06-03 下午5:18.
 */
@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

	static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

	private final Datastore datastore;

	public ApplicationRunner(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public void run(ApplicationArguments arguments) throws Exception
	{

		// 首次运行，执行一些初始化的工作
//		File lockFile = new File(System.getProperty("user.dir")+"/"+ appConfig.getDataDir()+"/node.lock");
//		if (!lockFile.exists()) {
//			lockFile.createNewFile();
//			// 创建默认钱包地址（挖矿地址）
//			Account minerAccount = personal.newAccount();
//			dataStore.setMinerAccount(minerAccount);
//			logger.info("Create miner account : {}", minerAccount);
//		}

	}

}
