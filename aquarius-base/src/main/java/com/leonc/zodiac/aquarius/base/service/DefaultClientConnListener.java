package com.leonc.zodiac.aquarius.base.service;

import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.rpc.NodeInfoMap;
import com.leonc.zodiac.aquarius.base.rpc.ClientConnListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultClientConnListener  implements ClientConnListener 
{
    private static Log logger = LogFactory.getLog(DefaultClientConnListener.class);

    private Node node;
    
    public DefaultClientConnListener(Node n) {
        this.node = n;
    }

    public void nodeConnected(String listenIp, int listenPort) {
        logger.info("connect finished:" + listenIp + ":" + listenPort);
    }

    public void nodeDisconnected(String listenIp, int listenPort) {
        logger.info("disconnect finished:" + listenIp + ":" + listenPort);

        //at present, we listen this event only in server module

        // NodeInfoMap router = node.getRouter();
        // String nodeId = router.getNodeIdFromServerAddr(listenIp, listenPort);
        // if(nodeId == null || nodeId.equals("")) return;

        // String ip = router.getClientIp(nodeId);
        // String port = router.getClientPort(nodeId);

        // this.nodeLeave(nodeId);
        // node.getServer().closeConn(ip, port);
        // router.unregisterNode(nodeId);
    }

    protected void nodeLeave(String nodeId) {
        logger.info("node leave: " + nodeId);
    }
}