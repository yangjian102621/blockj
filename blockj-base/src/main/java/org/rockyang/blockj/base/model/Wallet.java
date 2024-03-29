package org.rockyang.blockj.base.model;

import org.rockyang.blockj.base.crypto.ECKeyPair;
import org.rockyang.blockj.base.crypto.Keys;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * wallet entity
 *
 * @author yangjian
 */
public class Wallet implements Serializable
{

    private String address;
    private String pubKey;
    private String priKey;
    private BigDecimal balance;
    private long messageNonce;

    public Wallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException
    {
        this(Keys.createEcKeyPair());
    }

    public Wallet(ECKeyPair keyPair)
    {
        this.priKey = keyPair.exportPrivateKey();
        this.pubKey = Keys.publicKeyEncode(keyPair.getPublicKey().getEncoded());
        this.address = keyPair.getAddress();
        this.balance = BigDecimal.ZERO;
        this.messageNonce = 0;
    }

    public Wallet(String address, BigDecimal balance)
    {
        this.address = address;
        this.balance = balance;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPubKey()
    {
        return pubKey;
    }

    public void setPubKey(String pubKey)
    {
        this.pubKey = pubKey;
    }

    public String getPriKey()
    {
        return priKey;
    }

    public void setPriKey(String priKey)
    {
        this.priKey = priKey;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public long getMessageNonce()
    {
        return messageNonce;
    }

    public void setMessageNonce(long messageNonce)
    {
        this.messageNonce = messageNonce;
    }
}
