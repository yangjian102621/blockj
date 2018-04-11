package com.aizone.blockchain.consensus;

/**
 * <p> 工作量计算结果 </p>
 *
 * @author wangwei
 * @date 2018/02/04
 */
public class PowResult {

    /**
     * 计数器
     */
    private long nonce;
    /**
     * hash值
     */
    private String hash;
    /**
     * 目标难度值
     */
    private Integer target;

    public PowResult(long nonce, String hash, Integer target) {
        this.nonce = nonce;
        this.hash = hash;
        this.target = target;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
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
