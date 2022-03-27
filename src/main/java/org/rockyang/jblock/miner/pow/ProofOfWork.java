package org.rockyang.jblock.miner.pow;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.chain.BlockHeader;
import org.rockyang.jblock.crypto.Hash;
import org.rockyang.jblock.utils.ByteUtils;
import org.rockyang.jblock.utils.Numeric;

import java.math.BigInteger;

/**
 * 工作量证明实现
 * @author yangjian
 */
public class ProofOfWork {

    // 难度目标位, target=24 时大约 30 秒出一个区块
    public static final int TARGET_BITS = 18;

    private final BlockHeader blockHeader;

    // 难度目标值
    private final BigInteger target;

    /**
     * 创建新的工作量证明，设定难度目标值
     * 对1进行移位运算，将1向左移动 (256 - TARGET_BITS) 位，得到我们的难度目标值
     */
    public static ProofOfWork newProofOfWork(BlockHeader blockHeader) {
        BigInteger targetValue = BigInteger.valueOf(1).shiftLeft((256 - TARGET_BITS));
        return new ProofOfWork(blockHeader, targetValue);
    }

    private ProofOfWork(BlockHeader blockHeader, BigInteger target) {
        this.blockHeader = blockHeader;
        this.target = target;
    }

    // 运行工作量证明，开始挖矿，找到小于难度目标值的Hash
    public PowResult run() {
        long nonce = 0;
        String shaHex = "";
        while (nonce < Long.MAX_VALUE) {
            byte[] data = this.prepareData(nonce);
            shaHex = Hash.sha256Hex(data);
            if (new BigInteger(shaHex, 16).compareTo(this.target) < 0) {
                break;
            } else {
                nonce++;
            }
        }
        return new PowResult(nonce, shaHex, this.target);
    }

    // validate the pow result
    public boolean validate() {
        byte[] data = this.prepareData(this.blockHeader.getNonce());
        return new BigInteger(Hash.sha256Hex(data), 16).compareTo(this.target) < 0;
    }

    // 准备数据
    // 注意：在准备区块数据时，一定要从原始数据类型转化为byte[]，不能直接从字符串进行转换
    private byte[] prepareData(long nonce) {
        byte[] prevBlockHashBytes = {};
        if (StringUtils.isNotBlank(this.blockHeader.getPreviousHash())) {
            //这里要去掉 hash 值的　0x 前缀， 否则会抛出异常
            String prevHash = Numeric.cleanHexPrefix(this.blockHeader.getPreviousHash());
            prevBlockHashBytes = new BigInteger(prevHash, 16).toByteArray();
        }

        return ByteUtils.merge(
                prevBlockHashBytes,
                ByteUtils.toBytes(this.blockHeader.getTimestamp()),
                ByteUtils.toBytes(TARGET_BITS),
                ByteUtils.toBytes(nonce)
        );
    }

    public static BigInteger getTarget() {
        return BigInteger.valueOf(1).shiftLeft((256 - TARGET_BITS));
    }

}
