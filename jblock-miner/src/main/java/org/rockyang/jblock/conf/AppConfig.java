package org.rockyang.jblock.conf;

import org.rockyang.jblock.base.store.Datastore;
import org.rockyang.jblock.base.store.RocksDatastore;
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
