package com.leonc.zodiac.aquarius.base.service;

import com.leonc.zodiac.aquarius.base.rpc.Service;
import com.leonc.zodiac.aquarius.base.rpc.Command;
import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.rpc.NodeInfo;
import com.leonc.zodiac.aquarius.base.rpc.NodeInfoMap;
import com.leonc.zodiac.aquarius.base.message.MsgPeer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PeerToPeerService implements Service
{
    private static Log logger = LogFactory.getLog(PeerToPeerService.class);

    private Node node;

    public PeerToPeerService(Node n) {
        this.node = n;
    }

    public void join(Command cmd) {
        NodeInfoMap router = node.getRouter();

        MsgPeer.PeerJoin msg = (MsgPeer.PeerJoin)cmd.getMessage();
        MsgPeer.PeerInfo info = msg.getInfo();
        
        String nodeId = info.getNodeId();
        router.registerNodeType(nodeId, info.getNodeType()).
            registerServerAddr(nodeId, info.getListenIp(), info.getListenPort()).
            registerClientAddr(nodeId, cmd.getRemoteIp(), cmd.getRemotePort());

        node.connectToNode(info.getListenIp(), info.getListenPort());
        
        MsgPeer.PeerJoinResponse rsp = MsgPeer.PeerJoinResponse.newBuilder().
            setNodeId(node.getNodeId()).build();
        node.remoteCall(nodeId, "PeerToPeerService", "joinResponse", rsp);
    }

    public void joinResponse(Command cmd) {
        NodeInfoMap router = node.getRouter();

        MsgPeer.PeerJoinResponse msg = (MsgPeer.PeerJoinResponse)cmd.getMessage();
        String nodeId = msg.getNodeId();
        router.registerClientAddr(nodeId, cmd.getRemoteIp(), cmd.getRemotePort());
    }

    public void leave(Command cmd) {
        //we will isten this event in disconnect
    }
}