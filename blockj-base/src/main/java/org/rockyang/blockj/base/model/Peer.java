package org.rockyang.blockj.base.model;

import org.rockyang.blockj.base.crypto.Hash;

import java.io.Serializable;

/**
 * @author yangjian
 */
public class Peer implements Serializable {

	private String id;
	private String ip;
	private int port;

	public Peer(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
		this.id = Hash.sha3(this.toString());
	}

	public Peer()
	{
		this(null, 0);
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	@Override
	public String toString()
	{
		return String.format("%s:%s", getIp(), getPort());
	}
}
