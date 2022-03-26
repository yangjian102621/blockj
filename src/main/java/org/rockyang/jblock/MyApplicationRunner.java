//package org.rockyang.jblock;
//
//import org.rockyang.jblock.chain.Account;
//import org.rockyang.jblock.chain.Personal;
//import org.rockyang.jblock.conf.AppConfig;
//import org.rockyang.jblock.db.Datastore;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//
///**
// * @author yangjian
// * @since 2019-06-03 下午5:18.
// */
//@Component
//public class MyApplicationRunner implements ApplicationRunner {
//
//	static Logger logger = LoggerFactory.getLogger(MyApplicationRunner.class);
//
//	private final Datastore dataStore;
//
//	private final Personal personal;
//
//	private final AppConfig appConfig;
//
//	public MyApplicationRunner(AppConfig appConfig, Datastore dataStore, Personal personal)
//	{
//		this.appConfig = appConfig;
//		this.dataStore = dataStore;
//		this.personal = personal;
//	}
//
//	@Override
//	public void run(ApplicationArguments arguments) throws Exception
//	{
//
//		// 首次运行，执行一些初始化的工作
//		File lockFile = new File(System.getProperty("user.dir")+"/"+ appConfig.getDataDir()+"/node.lock");
//		if (!lockFile.exists()) {
//			lockFile.createNewFile();
//			// 创建默认钱包地址（挖矿地址）
//			Account minerAccount = personal.newAccount();
//			dataStore.setMinerAccount(minerAccount);
//			logger.info("Create miner account : {}", minerAccount);
//		}
//
//	}
//
//}
