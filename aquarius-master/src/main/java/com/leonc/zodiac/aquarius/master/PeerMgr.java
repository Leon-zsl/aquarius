package com.leonc.zodiac.aquarius.master;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentHashMap;

import com.leonc.zodiac.aquarius.base.message.Clock;
import com.leonc.zodiac.aquarius.base.message.PeerInfo;

public class PeerMgr
{
    private CopyOnWriteArraySet<PeerInfo> addList = new CopyOnWriteArraySet<PeerInfo>();
    private CopyOnWriteArraySet<String> rmList = new CopyOnWriteArraySet<String>();

    private ConcurrentHashMap<String, Peer> peerMap = new ConcurrentHashMap<String, Peer>();

    public void start() {
        //do nothing
    }
    
    public void close() {
        addList.clear();
        rmList.clear();

        for(Peer peer : peerMap) {
            peer.close();
        }

        clock.stop();
    }

    public void update() {
        doDelPeer();
        doAddPeer();

        for(Peer peer : peerMap.values()) {
            peer.update();
        }
    }

    public void addPeer(PeerInfo info) {
        if(info == null) return;
        if(info.getNodeId() == null || info.getNodeId().equals("")) return;

        addList.add(info);
    }

    public void delPeer(String nodeId) {
        rmList.add(nodeId);
    }

    public Peer getPeer(String nodeId) {
        return peerMap.get(nodeId);
    }

    public Peer[] getPeers() {
        return peerMap.values().toArray(new Peer[0]);
    }

    public Peer[] getPeersByType(String nodeType) {
        ArrayList<Peer> list = new ArrayList<Peer>();
        for(Peer peer : peerMap) {
            if(peer.getType().equals(nodeType)) {
                list.add(peer);
            }
        }
        return list.toArray(new Peer[0]);
    }

    private void doAddPeer() {
        for(PeerInfo info : addList) {
            if(peerMap.containsKey(info.getNodeId()))
                continue;
            peerMap.putIfAbsent(info.getNodeId(), new Peer(this, info));
        }
        addList.clear();
    }

    private void doDelPeer() {
        for(String id : rmList) {
            peerMap.remove(id);
        }
        rmList.clear();
    }
}