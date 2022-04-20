package org.rockyang.jblock.net.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tio.core.Node;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangjian
 */
@Component
public class NetConfig {

	public static final String SERVER_NAME = "jblock-tio-server";
	public static final String NODE_GROUP_NAME = "jblock-nodes";
	public static final Map<Node, Boolean> SERVERS = new ConcurrentHashMap<>(16);

	@Value("${tio.server.address}")
	private String serverAddress;
	@Value("${tio.server.port}")
	private int serverPort;
	@Value("${genesis.address}")
	private String genesisAddress;
	@Value("${genesis.port}")
	private int genesisPort;

	public String getServerAddress()
	{
		return serverAddress;
	}

	public void setServerAddress(String serverAddress)
	{
		this.serverAddress = serverAddress;
	}

	public int getServerPort()
	{
		return serverPort;
	}

	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}

	public String getGenesisAddress()
	{
		return genesisAddress;
	}

	public void setGenesisAddress(String genesisAddress)
	{
		this.genesisAddress = genesisAddress;
	}

	public int getGenesisPort()
	{
		return genesisPort;
	}

	public void setGenesisPort(int genesisPort)
	{
		this.genesisPort = genesisPort;
	}
}
