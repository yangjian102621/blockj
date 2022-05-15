package org.rockyang.blockj.conf;

import org.rockyang.blockj.base.store.Datastore;
import org.rockyang.blockj.base.store.RocksDatastore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangjian
 */
@Configuration
public class AppConfig {

	@Bean(value = "datastore")
	public Datastore getDataStore(MinerConfig minerConfig)
	{
		return new RocksDatastore(minerConfig.getRepo());
	}
}
