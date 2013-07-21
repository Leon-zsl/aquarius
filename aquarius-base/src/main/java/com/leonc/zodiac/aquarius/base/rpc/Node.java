package com.leonc.zodiac.aquarius.base.rpc;

import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;

import com.google.protobuf.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Node 
{
    private static Log logger = LogFactory.getLog(Node.class);

    private String nodeId = "";
    private String nodeType = "";

    private Server server = new Server(this);
    private Client client = new Client(this);
    private NodeInfoMap router = new NodeInfoMap();

    public Server getServer() { return this.server; }
    public Client getClient() { return this.client; }
    public NodeInfoMap getRouter() { return this.router; }

    public String getNodeId() { return this.nodeId; }
    public Node setNodeId(String id) {
        this.nodeId = id;
        return this;
    }

    public String getNodeType() { return this.nodeType; }
    public Node setNodeType(String tp) { 
        this.nodeType = tp;
        return this;
    }

    public Node setServerConnListener(ServerConnListener l) {
        this.server.setConnListener(l);
        return this;
    }

    public Node setClientConnListener(ClientConnListener l) {
        this.client.setConnListener(l);
        return this;
    }

    public String getListenIp() { return this.server.getListenIp(); }
    public int getListenPort() { return this.server.getListenPort(); }

    public synchronized void start(int listenPort) {
    	try {
    		String ip = InetAddress.getLocalHost().getHostAddress();
    		server.start(ip, listenPort, false);
    		client.start();
    	} catch(Exception ex) {
    		logger.error("exception: " + ex);
    	}
    }

    public synchronized void start(String listenIp, int listenPort, 
                                   boolean singleThread) {
        server.start(listenIp, listenPort, singleThread);
        client.start();
    }

    public synchronized void close() {
        this.server.close();
        this.client.close();
    }

    public Node registerService(Service sv) {
        this.server.registerService(sv);
        return this;
    }

    public void connectToNode(String serverIp, int serverPort) {
        this.client.connect(serverIp, serverPort);
    }

    public void disconnectFromNode(String serverIp, int serverPort) {
        this.client.disconnect(serverIp, serverPort);
    }

    public void remoteCall(String serverIp, int serverPort, String serviceName,
                           String methodName, Message msg) {
        this.client.remoteCall(serverIp, serverPort, serviceName, methodName, msg);
    }

    public void remoteCall(String nodeId, String serviceName, String methodName,
                           Message msg) {
        String addr = this.router.getServerAddr(nodeId);
        if(addr.equals("")) {
            logger.warn("can not find server addr for nodeId:" + nodeId);
            return;
        }
        String ip = this.router.ipFromAddr(addr);
        int port = this.router.portFromAddr(addr);
        this.remoteCall(ip, port, serviceName, methodName, msg);
    }

    public void response(String clientIp, int clientPort, String serviceName,
                         String methodName, Message msg) {
        String nodeId = this.router.getNodeIdFromClientAddr(clientIp, clientPort);
        if(nodeId == null || nodeId.equals("")) {
            logger.warn("can not find nodeid for client addr:" + clientIp + ":" + clientPort);
            return;
        }
        this.remoteCall(nodeId, serviceName, methodName, msg);
    }

    public void broadcast(String serviceName, String methodName, Message msg) {
        this.client.broadcast(serviceName, methodName, msg);
    }

    public void broadcastToOthers(String excludeIp, int exludedPort,
                                  String serviceName, String methodName, Message msg) {
        this.client.broadcastToOthers(excludeIp, exludedPort, serviceName, methodName, msg);
    }

    public void broadcastToOthers(String excludeId, String serviceName, 
                                  String methodName, Message msg) {
        String addr = this.router.getServerAddr(nodeId);
        if(addr.equals("")) {
            logger.warn("can not find server addr for nodeId" + nodeId);
            return;
        }
        String ip = this.router.ipFromAddr(addr);
        int port = this.router.portFromAddr(addr);
        this.client.broadcastToOthers(ip, port, serviceName, methodName, msg);
    }
}
