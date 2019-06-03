package org.rockyang.blockchain;

import org.junit.Test;

import java.math.BigInteger;

/**
 * 临时测试文件，测试各种其他测试代码
 * @author yangjian
 * @since 2018-04-07 下午8:38.
 */
public class TempTest {

	@Test
	public void run() {
//		BigInteger targetValue = BigInteger.valueOf(1).shiftLeft((256 - 15));
//		BigInteger bigInteger = BigInteger.valueOf(1).shiftLeft((224));
//		System.out.println(targetValue.divide(bigInteger));
//		System.out.println(BigInteger.ONE);

		//System.out.println("blocks_"+1);
		BigInteger bigInteger = new BigInteger("01010", 10);
		System.out.println(bigInteger);

	}


}
