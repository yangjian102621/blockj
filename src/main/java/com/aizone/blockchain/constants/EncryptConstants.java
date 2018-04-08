package com.aizone.blockchain.constants;

/**
 * 加密算法用到的常量
 * @author yangjian
 * @since 18-4-8
 */
public interface EncryptConstants {
	/**
	 * 密钥生成算法，默认为椭圆曲线算法
	 */
	String KEY_GEN_ALGORITHM = "ECDSA";
	String KEY_GEN_PROVIDER = "BC";
	/**
	 * 安全随机数生成算法
	 */
	String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
	String ECGEN_PARAM_SPEC = "prime192v1";
}
