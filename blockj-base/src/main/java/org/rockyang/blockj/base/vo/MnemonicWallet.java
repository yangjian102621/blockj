package org.rockyang.blockj.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.rockyang.blockj.base.model.Wallet;

import java.io.Serializable;

/**
 * @author yangjian
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class MnemonicWallet implements Serializable
{
    private String mnemonic;
    private Wallet wallet;

    public MnemonicWallet()
    {
    }

    public MnemonicWallet(String mnemonic, Wallet wallet)
    {
        this.mnemonic = mnemonic;
        this.wallet = wallet;
    }

    public String getMnemonic()
    {
        return mnemonic;
    }

    public Wallet getWallet()
    {
        return wallet;
    }
}
