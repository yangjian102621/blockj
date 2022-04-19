package org.rockyang.jblock.chain.sync;

import org.rockyang.jblock.base.utils.ThreadUtils;
import org.rockyang.jblock.chain.event.SyncBlockEvent;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class Syncer {

	private volatile boolean running;

	public Syncer()
	{
		this.running = true;
	}

	//	@EventListener(ApplicationReadyEvent.class)
	public void run()
	{
		new Thread(() -> {
			while (true) {
				if (running) {
					ApplicationContextProvider.publishEvent(new SyncBlockEvent(0));
				}
				ThreadUtils.niceSleep(5);
			}
		}).start();
	}

	public void start()
	{
		this.running = true;
	}

	public void stop()
	{
		this.running = false;
	}

}
