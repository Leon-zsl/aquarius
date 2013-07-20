package com.leonc.zodiac.aquarius.base.service;

import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.rpc.NodeInfoMap;
import com.leonc.zodiac.aquarius.base.rpc.ServerConnListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultServerConnListener implements ServerConnListener 
{
    private static Log logger = LogFactory.getLog(DefaultClientConnListener.class);

    private Node node;
    
    public DefaultServerConnListener(Node n) {
        this.node = n;
    }

    public void nodeConnected(String ip, int port) {
        logger.info("new node connected:" + ip + ":" + port);
    }

    public void nodeDisconnected(String ip, int port) {
        logger.info("node disconnected: " + ip + ":" + port);
        
        NodeInfoMap router = node.getRouter();
        String nodeId = router.getNodeIdFromClientAddr(ip, port);
        if(nodeId == null || nodeId.equals("")) return;

        String remoteIp = router.getServerIp(nodeId);
        int remotePort = router.getServerPort(nodeId);

        this.nodeLeave(nodeId);
        node.disconnectFromNode(remoteIp, remotePort);
        router.unregisterNode(nodeId);
    }

    protected void nodeLeave(String nodeId) {
        logger.info("node leave: " + nodeId);
    }
}
