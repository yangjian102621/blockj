package org.rockyang.jblock.chain.service.impl;

import org.rockyang.jblock.chain.service.PeerService;
import org.rockyang.jblock.net.base.Peer;
import org.rockyang.jblock.store.Datastore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangjian
 */
@Service
public class PeerServiceImpl implements PeerService {

	private final Datastore datastore;
	private final String PEER_PREFIX = "/peers/";

	public PeerServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public void addPeer(Peer peer)
	{
		if (hasPeer(peer)) {
			return;
		}
		datastore.put(PEER_PREFIX + peer.toString(), peer);
	}

	@Override
	public List<Peer> getPeers()
	{
		return datastore.search(PEER_PREFIX);
	}

	@Override
	public void removePeer(Peer peer)
	{
		datastore.delete(peer.toString());
	}

	@Override
	public boolean hasPeer(Peer peer)
	{
		return datastore.get(PEER_PREFIX + peer.toString()).isPresent();
	}
}
