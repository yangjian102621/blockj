package org.rockyang.jblock.chain;

import org.rockyang.jblock.crypto.ECKeyPair;
import org.rockyang.jblock.crypto.Keys;

import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * wallet entity
 * @author yangjian
 */
public class Wallet implements Serializable {

	protected String address;
	private String pubKey;
	protected String priKey;

	public Wallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException
	{
		ECKeyPair keyPair = Keys.createEcKeyPair();
		this.priKey = keyPair.exportPrivateKey();
		this.pubKey = keyPair.getPublicKey().toString();
		this.address = keyPair.getAddress();
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
}
