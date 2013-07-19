package com.leonc.zodiac.aquarius.master.service;

import com.google.protobuf.Message;

import com.leonc.zodiac.aquarius.base.rpc.Service;
import com.leonc.zodiac.aquarius.base.rpc.Command;
import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.rpc.NodeInfo;
import com.leonc.zodiac.aquarius.base.rpc.NodeInfoMap;
import com.leonc.zodiac.aquarius.base.message.MsgPeer;

import com.leonc.zodiac.aquarius.master.App;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PeerToMasterService implements Service
{
    private static Log logger = LogFactory.getLog(PeerToMasterService.class);

    public void register(Command cmd) {
        Message msg = cmd.getMessage();
        if(!(msg instanceof MsgPeer.PeerRegister)) {
            logger.info("register message is illeagal format");
            return;
        }

        Node node = App.getInstance().getNode();
        NodeInfoMap router = node.getRouter();

        MsgPeer.PeerRegister req = (MsgPeer.PeerRegister)msg;
        String nodeType = req.getNodeType();
        String listenIp = req.getListenIp();
        int listenPort = req.getListenPort();

        String[] ids = router.getNodeIdsFromNodeType(nodeType);
        String nodeId = "";
        if(ids == null)
            nodeId = nodeType + "_0";
        else
            nodeId = nodeType + "_" + ids.length;

        //add to router map
        router.registerNodeType(nodeId, nodeType).
            registerServerAddr(nodeId, listenIp, listenPort).
            registerClientAddr(nodeId, cmd.getRemoteIp(), cmd.getRemotePort());

        //connect to peer
        node.connectToNode(listenIp, listenPort);

        //response to peer
        MsgPeer.PeerRegisterResponse rsp = MsgPeer.PeerRegisterResponse.newBuilder().setNodeId(nodeId).build();
        node.remoteCall(nodeId, "MasterToPeerService", "registerRepsonse", rsp);
    }

    public void unregister(Command cmd) {
        Node node = App.getInstance().getNode();
        NodeInfoMap router = node.getRouter();
        
        String nodeId = router.getNodeIdFromClientAddr(cmd.getRemoteIp(), 
                                                       cmd.getRemotePort());

        //disconnect 
        String listenIp = router.getServerIp(nodeId);
        int listenPort = router.getServerPort(nodeId);
        node.disconnectFromNode(listenIp, listenPort);

        //remove from router map
        router.unregisterNode(nodeId);
    }

    public void fetchNodeList(Command cmd) {
        Node node = App.getInstance().getNode();
        NodeInfoMap router = node.getRouter();
        NodeInfo[] infos = router.getNodeInfos();

        MsgPeer.FetchPeerListResponse.Builder bd = MsgPeer.FetchPeerListResponse.newBuilder();
        for(NodeInfo info : infos) {
            MsgPeer.PeerInfo pi = MsgPeer.PeerInfo.newBuilder().
                setNodeId(info.nodeId).setNodeType(info.nodeType).
                setListenIp(router.ipFromAddr(info.serverAddr)).
                setListenPort(router.portFromAddr(info.serverAddr)).build();
            bd.addPeerList(pi);
        }

        node.response(cmd.getRemoteIp(), cmd.getRemotePort(),
                      "MasterToPeerService", "fetchPeerListResponse",
                      bd.build());
    }
}