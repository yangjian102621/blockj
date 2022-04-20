package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.base.model.Peer;

import java.util.List;

/**
 * @author yangjian
 */
public interface PeerService {
	void addPeer(Peer peer);

	List<Peer> getPeers();

	void removePeer(Peer peer);

	boolean hasPeer(Peer peer);
}
