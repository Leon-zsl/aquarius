package com.leonc.zodiac.aquarius.base.rpc;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;

import com.google.protobuf.Message;

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

    public Node(String type) {
        this.serverInfo.nodeType = type;
    }

    public synchronized void start(String nodeId, int listenPort) {
        initServer(nodeId, listenPort, false);
        initClient();
    }

    public synchronized void start(String nodeId, 
                                   int listenPort, 
                                   boolean singleThread) {
        initServer(nodeId, listenPort, singleThread);
        initClient();
    }

    public synchronized void close() {
        this.server.close();
        this.client.close();
        
        this.clientInfoList.clear();
    }

    public synchronized void initServer(String id, 
                                        int port, 
                                        boolean singleThread) {
        server = new Server(this, this.serverInfo.nodeId, port);
        server.start(singleThread);
        
        this.serverInfo.nodeId = id;
    }

    public synchronized void initClient() {
        client = new Client(this);
        client.start();
    }

    public Server getServer() { return this.server; }
    public Client getClient() { return this.client; }

    public String getNodeId() { return this.server.getId(); }
    
    public void registerService(Service sv) {
        this.server.registerService(sv);
    }

    public void remoteCall(Command cmd) {
        this.client.remoteCall(cmd);
    }
    
    public void remoteCall(String nodeId, String serviceName, String methodName,
                           Message message) {
        this.remoteCall(new Command(nodeId, serviceName, methodName, message));
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
