package org.rockyang.blockj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yangjian
 */
@SpringBootApplication
public class Application {
	public static void main(String[] args) throws Exception
	{
		AppRunner runner = new AppRunner(args);
		if (!runner.preRun()) {
			runner.cleanRepo();
			System.exit(0);
		}
		SpringApplication.run(Application.class, args);
	}
}
