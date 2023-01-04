package org.rockyang.blockj.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.base.model.Peer;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.chain.event.NewPeerEvent;
import org.rockyang.blockj.conf.ApplicationContextProvider;
import org.rockyang.blockj.net.client.P2pClient;
import org.rockyang.blockj.net.conf.NetConfig;
import org.rockyang.blockj.service.PeerService;
import org.springframework.web.bind.annotation.*;
import org.tio.core.Node;

import java.util.List;

/**
 * chain api handler
 */
@RestController
@RequestMapping("/net")
public class NetController
{
    private final PeerService peerService;
    private final NetConfig netConfig;
    private final P2pClient client;

    public NetController(PeerService peerService, NetConfig netConfig, P2pClient client)
    {
        this.peerService = peerService;
        this.netConfig = netConfig;
        this.client = client;
    }

    @GetMapping("/peers")
    public JsonVo peers()
    {
        List<Peer> peers = peerService.getPeers();
        return new JsonVo<>(JsonVo.SUCCESS, peers);
    }

    @GetMapping("/listen")
    public JsonVo listen()
    {
        String listen = String.format("%s:%d", netConfig.getServerAddress(), netConfig.getServerPort());
        return new JsonVo(JsonVo.SUCCESS, listen);
    }

    @PostMapping("/connect")
    public JsonVo connect(@RequestBody JSONObject params) throws Exception
    {
        String address = params.getString("address");
        if (StringUtils.isBlank(address)) {
            return new JsonVo(JsonVo.FAIL, "Invalid peer address");
        }

        String[] split = address.split(":");
        if (split.length != 2) {
            return new JsonVo(JsonVo.FAIL, "Invalid format for peer address");
        }

        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        Peer peer = new Peer(ip, port);
        if (peerService.hasPeer(peer)) {
            return new JsonVo(JsonVo.FAIL, String.format("Peer %s is already connected", peer));
        }

        // store peer
        peerService.addPeer(peer);
        // try to connect peer
        if (client.connect(new Node(peer.getIp(), peer.getPort()))) {
            // fire new peer connected event
            ApplicationContextProvider.publishEvent(new NewPeerEvent(peer));
        }

        return new JsonVo(JsonVo.SUCCESS, String.format("Connected peer %s successfully", peer));
    }
}
