package org.rockyang.blockj.miner.pow;

import java.math.BigInteger;

/**
 * PoW 计算结果
 * @author yangjian
 */
public class PowResult {

    // pow value
    private final long nonce;
    // hash of the new block
    private final String hash;
    // pow target
    private final BigInteger target;

    public PowResult(long nonce, String hash, BigInteger target) {
        this.nonce = nonce;
        this.hash = hash;
        this.target = target;
    }

    public long getNonce() {
        return nonce;
    }

    public String getHash() {
        return hash;
    }

    public BigInteger getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "PowResult{" +
                "nonce=" + nonce +
                ", hash='" + hash + '\'' +
                ", target=" + target +
                '}';
    }
}
