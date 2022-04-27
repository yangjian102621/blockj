package org.rockyang.jblock.service;

import org.rockyang.jblock.base.model.Peer;

import java.util.List;

/**
 * @author yangjian
 */
public interface PeerService {
	String PEER_PREFIX = "/peers/";

	boolean addPeer(Peer peer);

	List<Peer> getPeers();

	boolean removePeer(Peer peer);

	boolean hasPeer(Peer peer);
}
