package com.leonc.zodiac.aquarius.base.rpc;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Node 
{
    public class NodeInfo {
        public  String nodeId = "";
        public  String nodeType = "";

        public NodeInfo() {
            this.nodeId = "";
            this.nodeType = "";
        }

        public NodeInfo(String id, String type) {
            this.nodeId = id;
            this.nodeType = type;
        }
    }

    private static Log logger = LogFactory.getLog(Node.class);

    private Server server = null;
    private Client client = null;

    private NodeInfo serverInfo = new NodeInfo();
    private CopyOnWriteArrayList<NodeInfo> clientInfoList = new CopyOnWriteArrayList<NodeInfo>();

    public Server getServer() { return this.server; }
    public Client getClient() { return this.client; }

    public void initServer(String id, int port) {
        server = new Server(this, this.serverInfo.nodeId, port);
        server.start();
        
        this.serverInfo.nodeId = id;
    }

    public void initClient() {
        client = new Client(this);
        client.start();
    }

    public void close() {
        this.server.close();
        this.client.close();
        
        this.clientInfoList.clear();
    }

    public Node(String type) {
        this.serverInfo.nodeType = type;
    }

    public void connectToNode(String nodeid, String nodetype, String ip, int port) {
        if(this.getClientNode(nodeid) != null) {
            logger.info("node exists:" + nodeid);
            return;
        }
        this.client.connect(nodeid, ip, port);
        this.clientInfoList.add(new NodeInfo(nodeid, nodetype));
    }

    public void disconnectFromNode(String nodeid) {
        this.client.disconnect(nodeid);
        NodeInfo n = getClientNode(nodeid);
        this.clientInfoList.remove(n);
    }

    public void sendPacket(String nodeid, String service, byte[] args) {
        this.client.sendPacket(nodeid, service, args);
    }

    public NodeInfo getClientNode(String nodeid) {
        for(NodeInfo info : clientInfoList) {
            if(info.nodeId == nodeid) 
                return info;
        }
        return null;
    }

    public String getClientType(String nodeid) {
        NodeInfo info = getClientNode(nodeid);
        if(info == null)
            return "";
        else
            return info.nodeType;
    }

    public NodeInfo[] getClientNodesByType(String type) {
        ArrayList<NodeInfo> list = new ArrayList<NodeInfo>();
        for(NodeInfo info : clientInfoList) {
            if(info.nodeType == type)
                list.add(info);
        }
        NodeInfo[] arr = new NodeInfo[list.size()];
        return list.toArray(arr);
    }
}
