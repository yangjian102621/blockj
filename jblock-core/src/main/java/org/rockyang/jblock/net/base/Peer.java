package org.rockyang.jblock.net.base;

import org.tio.core.Node;

import java.io.Serializable;

/**
 * @author yangjian
 */
public class Peer extends Node implements Serializable {

	public Peer(String ip, int port) {
		super(ip, port);
	}

	public Peer() {
		super(null, 0);
	}

	@Override
	public String toString()
	{
		return String.format("%s:%s", getIp(), getPort());
	}
}
