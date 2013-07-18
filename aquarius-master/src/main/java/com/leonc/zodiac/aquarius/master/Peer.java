package com.leonc.zodiac.aquarius.master;

import com.leonc.zodiac.aquarius.base.util.Clock;

import com.leonc.zodiac.aquarius.base.message.PeerId;
import com.leonc.zodiac.aquarius.base.message.PeerInfo;

public class Peer
{
    private PeerMgr mgr = null;

    private String nodeId = "";
    private String nodeType = "";
    private String ip = "";
    private int port = "";

    private long lastHBTime = 0;
    private Clock clock = new Clock();

    public Peer(PeerMgr mgr, NodeInfo info) {
        this.mgr = mgr;
        this.nodeId = info.getNodeId();
        this.nodeType = info.getNodeType();
        this.ip = info.getIp();
        this.port = info.getPort();

        clock.start();
        lastHBTime = clock.frameTime();
    }

    public String getId() { return this.nodeId; }
    public String getType() { return this.nodeType; }
    public String getIp() { return this.ip; }
    public String getPort() { return this.port; }
    public PeerInfo getInfo() {
        return PeerInfo.newBuilder().
            setNodeId(nodeId).
            setNodeType(nodeType).
            setIp(ip).
            setPort(port).
            build();
    }

    public void update() {
        clock.update();
        checkAlive();
    }

    public void hearbeat() {
        lastHBTime = clock.frameTime();
    }

    private void checkAlive() {
        if((clock.frameTime() - lastHBTime) > Global.DEATH_PEER_TIME) {
            PeerId id = PeerId.newBuilder().setNodeId(this.nodeId).build();
            App.getInstance().getNode().broadcastToOthers(this.nodeId,
                                                          "PeerService", 
                                                          "peerLeave",
                                                          id);
            this.mgr.delPeer(this.nodeId);
        }
    }
}