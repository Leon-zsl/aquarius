package com.leonc.zodiac.aquarius.base.service;

import com.leonc.zodiac.aquarius.base.rpc.Service;
import com.leonc.zodiac.aquarius.base.rpc.Command;
import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.rpc.NodeInfo;
import com.leonc.zodiac.aquarius.base.rpc.NodeInfoMap;
import com.leonc.zodiac.aquarius.base.message.MsgPeer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MasterToPeerService implements Service
{
    private static Log logger = LogFactory.getLog(MasterToPeerService.class);

    private Node node;

    public MasterToPeerService(Node n) { 
        this.node = n;
    }

    public void registerResponse(Command cmd) {
        NodeInfoMap router = node.getRouter();
        
        MsgPeer.PeerRegisterResponse msg = (MsgPeer.PeerRegisterResponse)cmd.getMessage();
        node.setNodeId(msg.getYourNodeId());
        router.registerClientAddr(msg.getMyNodeId(), 
                                  cmd.getRemoteIp(), 
                                  cmd.getRemotePort());

        MsgPeer.FetchPeerList req = MsgPeer.FetchPeerList.newBuilder().build();
        node.remoteCall(msg.getMyNodeId(), 
                        "PeerToMasterService", 
                        "fetchNodeList", 
                        req);
    }

    public void fetchPeerListResponse(Command cmd) {
        NodeInfoMap router = node.getRouter();

        MsgPeer.FetchPeerListResponse rsp = (MsgPeer.FetchPeerListResponse)cmd.getMessage();
        int cnt = rsp.getPeerCount();
        if(cnt == 0) return;

        for(int i = 0; i < cnt; ++i) {
            MsgPeer.PeerInfo info = rsp.getPeer(i);

            router.registerNodeType(info.getNodeId(), 
                                    info.getNodeType()).
                registerServerAddr(info.getNodeId(), 
                                   info.getListenIp(), 
                                   info.getListenPort());
            node.connectToNode(info.getListenIp(), info.getListenPort());

            MsgPeer.PeerInfo selfInfo = MsgPeer.PeerInfo.newBuilder().
                setNodeId(node.getNodeId()).
                setNodeType(node.getNodeType()).
                setListenIp(node.getListenIp()).
                setListenPort(node.getListenPort()).build();
            MsgPeer.PeerJoin msg = MsgPeer.PeerJoin.newBuilder().
                setInfo(selfInfo).build();
            node.remoteCall(info.getNodeId(), "PeerToPeerService", "join", msg);
        }
    }
}