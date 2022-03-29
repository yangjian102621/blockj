package org.rockyang.jblock.chain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * wallet account
 * @author yangjian
 */
public class Account implements Serializable {

	private String address;
	private BigDecimal balance;
	private String publicKey;

	public Account(String address, BigDecimal balance)
	{
		this.address = address;
		this.balance = balance;
	}

	public Account() {}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public BigDecimal getBalance()
	{
		return balance;
	}

	public void setBalance(BigDecimal balance)
	{
		this.balance = balance;
	}

	public String getPublicKey()
	{
		return publicKey;
	}

	public void setPublicKey(String publicKey)
	{
		this.publicKey = publicKey;
	}
}
