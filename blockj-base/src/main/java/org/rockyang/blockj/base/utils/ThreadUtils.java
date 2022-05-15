package org.rockyang.blockj.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yangjian
 */
public class ThreadUtils {

	private static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

	public static void niceSleep(int secs)
	{
		try {
			Thread.sleep(secs * 1000L);
		} catch (InterruptedException e) {
			logger.warn("received interrupt while trying to sleep in mining cycle");
		}
	}
}
