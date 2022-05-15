package org.rockyang.blockj.service.impl;

import org.rockyang.blockj.base.model.Peer;
import org.rockyang.blockj.base.store.Datastore;
import org.rockyang.blockj.service.PeerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangjian
 */
@Service
public class PeerServiceImpl implements PeerService {

	private final Datastore datastore;

	public PeerServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public boolean addPeer(Peer peer)
	{
		return datastore.put(PEER_PREFIX + peer.toString(), peer);
	}

	@Override
	public List<Peer> getPeers()
	{
		return datastore.search(PEER_PREFIX);
	}

	@Override
	public boolean removePeer(Peer peer)
	{
		return datastore.delete(peer.toString());
	}

	@Override
	public boolean hasPeer(Peer peer)
	{
		return datastore.get(PEER_PREFIX + peer.toString()).isPresent();
	}
}
