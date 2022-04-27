package org.rockyang.jblock;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yangjian
 */
public class TempTest {

	private final ReentrantLock lock = new ReentrantLock();

	@Test
	public void run() throws InterruptedException
	{
		boolean b = true;
		try {
			lock.lock();
			if (b) {
				System.out.println("returned");
				return;
			}
		} finally {
			System.out.println("release");
			lock.unlock();
		}
	}

	public synchronized void m1() throws InterruptedException
	{
		System.out.println(Thread.currentThread().getName() + " enter method 1");
		Thread.sleep(5000);
		System.out.println(Thread.currentThread().getName() + " leave method 1");
	}

	public void m2() throws InterruptedException
	{
		lock.lock();
		System.out.println(Thread.currentThread().getName() + " enter method2");
		Thread.sleep(5000);
		System.out.println(Thread.currentThread().getName() + " leave method2");
		lock.unlock();
	}

}
