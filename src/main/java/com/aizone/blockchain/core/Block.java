package com.aizone.blockchain.core;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 区块对象
 * @author yangjian
 * @since 18-4-6
 */
public class Block {

	/**
	 * 区块高度
	 */
	private Integer index;
	/**
	 * 区块所包含的交易记录
	 */
	private List<Transaction> transactions;
	/**
	 * 难度指标
	 */
	private BigInteger difficulty;
	/**
	 * PoW 问题的答案
	 */
	private Integer nonce;
	/**
	 * 时间戳
	 */
	private Date timestamp;
	/**
	 * 上一个区块的 hash 地址
	 */
	private String previousHash;
}
