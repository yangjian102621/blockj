package org.rockyang.jblock.base.model;

import org.rockyang.jblock.base.crypto.ECKeyPair;
import org.rockyang.jblock.base.crypto.Keys;

import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * wallet entity
 *
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
		this.pubKey = Keys.publicKeyEncode(keyPair.getPublicKey().getEncoded());
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
