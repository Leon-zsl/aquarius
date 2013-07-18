package com.leonc.zodiac.aquarius.master.service;

import com.leonc.zodiac.aquarius.base.rpc.Service;
import com.leonc.zodiac.aquarius.base.rpc.Command;

import com.leonc.zodiac.aquarius.base.message.MsgCommon;
import com.leonc.zodiac.aquarius.base.message.MsgPeer;

import com.leonc.zodiac.aquarius.base.MsgErrCode;

public class PeerService implements Service 
{
    public void heartbeat(Command cmd) {
        Peer peer = App.getInstance().getPeerMgr().getPeer(cmd.getRemoteId());
        if(peer != null)
            peer.heartbeat();
    }

    public void joinIn(Command cmd) {
        PeerMgr mgr = App.getInstance().getPeerMgr();
        if(mgr.getPeer(cmd.getRemoteId() == null)) {
        //     MsgCommon.BaseResponse rsp = MsgCommon.BaseResponse.
        //         newBuilder().setErrorCode(MsgErrCode.PEER_EXISTS);
        //     App.getInstance().getNode().remoteCall(cmd.getRemoteId(),
        //                                            "PeerService", 
        //                                            "joinInResult",
        //                                            rsp);
        // } else {
            MsgPeer.PeerInfo info = (MsgPeer.PeerInfo)cmd.getMessage();
            App.getInstance().getNode().connectToNode(info.getNodeId(),
                                                      info.getNodeType(),
                                                      info.getIp(),
                                                      info.getPort());

            App.getInstance().getPeerMgr().addPeer(info);

            MsgCommon.BaseResponse rsp = MsgCommon.BaseResponse.
                newBuilder().setErrorCode(MsgErrCode.NO_ERR);
            App.getInstance().getNode().remoteCall(cmd.getRemoteId(),
                                                   "PeerService", 
                                                   "joinInResult",
                                                   rsp);
        }
    }

    public void leave(Command cmd) {
        String id = cmd.getRemoteId();
        PeerId rsp = MsgCommon.StringMsg.newBuilder().setNodeId(id).build();
        App.getInstance().getNode().broadcastToOthers(id,
                                                      "PeerService", 
                                                      "peerLeave",
                                                      rsp);
        App.getInstance().getPeerMgr().delPeer(id);
        App.getInstance().getNode().disconnectFromNode(id);
    }

    public void fetchPeerList(Command cmd) {
        Peer[] peers = App.getInstance().getPeerMgr().getPeers();

        MsgPeer.PeerInfoList infoList = null;
        if(peers == null) {
            infoList = MsgPeer.PeerInfoList.newBuilder().build();
        } else {
            MsgPeer.PeerInfoList.Builder bd = MsgPeer.PeerInfoList.newBuilder();
            for(int i = 0; i < peers.length; i++) {
                Peer peer = peers[i];
                MsgPeer.PeerInfo info = MsgPeer.PeerInfo.newBuilder().
                    setNodeId(peer.getId()).
                    setNodeType(peer.getType()).
                    setIp(peer.getIp()).
                    setPort(peer.getPort()).
                    build();
                bd.addPeerList(info);
            }
            infoList = bd.build();
        }
        App.getInstance().getNode().remoteCall(cmd.getRemoteId(),
                                               "PeerService",
                                               "fetchPeerListResponse",
                                               infoList);
    }

    public void getPeerById(Command cmd) {
        string id = ((MsgCommon.StringMsg)cmd.getMessage()).getValue();
        Peer peer = App.getInstance().getPeerMgr().getPeer(id);

        MsgPeer.PeerInfo info = null;
        if(peer == null) {
            info = MsgPeer.PeerInfo.newBuilder().setNodeId("").build();
        } else {
            info = MsgPeer.PeerInfo.newBuilder().setNodeId(peer.getId()).
                setNodeType(peer.getType()).
                setIp(peer.getIp()).
                setPort(peer.getPort()).
                build();
        }
        App.getInstance().getNode().remoteCall(cmd.getRemoteId(),
                                               "PeerService",
                                               "getPeerResponse",
                                               info);
    }

    public void getPeersByType(Command cmd) {
        string type = ((MsgCommon.StringMsg)cmd.getMessage()).getValue();
        Peer[] peers = App.getInstance().getPeerMgr().getPeersByType(type);

        MsgPeer.PeerInfoList infoList = null;
        if(peers == null) {
            infoList = MsgPeer.PeerInfoList.newBuilder().build();
        } else {
            MsgPeer.PeerInfoList.Builder bd = MsgPeer.PeerInfoList.newBuilder();
            for(int i = 0; i < peers.length; i++) {
                Peer peer = peers[i];
                MsgPeer.PeerInfo info = MsgPeer.PeerInfo.newBuilder().
                    setNodeId(peer.getId()).
                    setNodeType(peer.getType()).
                    setIp(peer.getIp()).
                    setPort(peer.getPort()).
                    build();
                bd.addPeerList(info);
            }
            infoList = bd.build();
        }
        App.getInstance().getNode().remoteCall(cmd.getRemoteId(),
                                               "PeerService",
                                               "getPeersByTypeResponse",
                                               infoList);
    }
}
