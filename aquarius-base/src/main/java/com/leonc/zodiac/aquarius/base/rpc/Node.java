package com.leonc.zodiac.aquarius.base.rpc;

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
    private PeerRouter router = new PeerRouter();

    public Server getServer() { return this.server; }
    public Client getClient() { return this.client; }
    public PeerRouter getRouter() { return this.router; }

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
    
    public synchronized void start(int listenPort, 
                                   ServerConnListener sl,
                                   ClientConnListener cl) {
        server.start(listenPort, sl, false);
        client.start(cl);
    }

    public synchronized void start(int listenPort, 
                                   ServerConnListener sl,
                                   ClientConnListener cl,
                                   boolean singleThread) {
        server.start(listenPort, sl, singleThread);
        client.start(cl);
    }

    public synchronized void close() {
        this.server.close();
        this.client.close();
    }

    public String getPeerType(String nodeId) {
        return this.router.getNodeType(nodeId);
    }

    public void setPeerType(String nodeId, String nodeType) {
        this.router.registerNodeType(nodeId, nodeType);
    }

    public String getPeerServerIp(String nodeId) {
        return this.router.getServerIp(nodeId);
    }

    public int getPeerServerPort(String nodeId) {
        return this.router.getServerPort(nodeId);
    }

    public void setPeerServerAddr(String nodeId, String ip, int port) {
        this.router.registerServerAddr(nodeId, ip, port);
    }

    public String getPeerClientIp(String nodeId) {
        return this.router.getClientIp(nodeId);
    }

    public int getPeerClientPort(String nodeId) {
        return this.router.getClientPort(nodeId);
    }

    public void setPeerClientAddr(String nodeId, String ip, int port) {
        this.router.registerClientAddr(nodeId, ip, port);
    }

    public void registerService(Service sv) {
        this.server.registerService(sv);
    }

    public void connectToNode(String ip, int port) {
        this.client.connect(ip, port);
    }

    public void disconnectFromNode(String ip, int port) {
        this.client.disconnect(ip, port);
    }

    public void broadcast(String serviceName, String methodName, Message msg) {
        this.client.broadcast(serviceName, methodName, msg);
    }

    public void remoteCall(String serverIp, int serverPort, String serviceName,
                           String methodName, Message msg) {
        this.client.remoteCall(serverIp, serverPort, serviceName, methodName, msg);
    }

    public void remoteCall(String nodeId, String serviceName, String methodName,
                           Message msg) {
        String addr = this.router.getServerAddr(nodeId);
        if(addr.equals("")) {
            logger.warn("can not find server addr for nodeId" + nodeId);
            return;
        }
        String ip = this.router.ipFromAddr(addr);
        int port = this.router.portFromAddr(addr);
        this.remoteCall(ip, port, serviceName, methodName, msg);
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
