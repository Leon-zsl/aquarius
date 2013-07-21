package com.leonc.zodiac.aquarius.base.rpc;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.Integer;

public class NodeInfoMap
{
    private ConcurrentHashMap<String, NodeInfo> nodeMap = new ConcurrentHashMap<String, NodeInfo>();

    public NodeInfoMap unregisterNode(String nodeId) {
        nodeMap.remove(nodeId);
        return this;
    }

    public NodeInfoMap registerServerAddr(String nodeId, String ip, int port) {
        String addr = ip + ":" + port;
        registerServerAddr(nodeId, addr);
        return this;
    }

    public NodeInfoMap registerClientAddr(String nodeId, String ip, int port) {
        String addr = ip + ":" + port;
        registerClientAddr(nodeId, addr);
        return this;
    }

    public synchronized NodeInfoMap registerServerAddr(String nodeId, String addr) {
        NodeInfo info = nodeMap.get(nodeId);
        if(info == null) {
            info = new NodeInfo();
            info.nodeId = nodeId;
            nodeMap.putIfAbsent(nodeId, info);
        }
        info.serverAddr = addr;

        return this;
    }

    public synchronized NodeInfoMap registerClientAddr(String nodeId, String addr) {
        NodeInfo info = nodeMap.get(nodeId);
        if(info == null) {
            info = new NodeInfo();
            info.nodeId = nodeId;
            nodeMap.putIfAbsent(nodeId, info);
        }
        info.clientAddr = addr;

        return this;
    }

    public synchronized NodeInfoMap registerNodeType(String nodeId, String nodeType) {
        NodeInfo info = nodeMap.get(nodeId);
        if(info == null) {
            info = new NodeInfo();
            info.nodeId = nodeId;
            nodeMap.putIfAbsent(nodeId, info);
        }
        info.nodeType = nodeType;

        return this;
    }

    public String[] getNodeIds() {
        return nodeMap.keySet().toArray(new String[0]);
    }
    
    public NodeInfo[] getNodeInfos() {
        return nodeMap.values().toArray(new NodeInfo[0]);
    }

    public String getNodeIdFromServerAddr(String addr) {
        for(NodeInfo info : nodeMap.values()) {
            if(info.serverAddr.equals(addr))
                return info.nodeId;
        }
        return "";
    }

    public String getNodeIdFromServerAddr(String ip, int port) {
        String addr = ip + ":" + port;
        getNodeIdFromServerAddr(addr);
        return "";        
    }

    public String getNodeIdFromClientAddr(String addr) {
        for(NodeInfo info : nodeMap.values()) {
            if(info.clientAddr.equals(addr))
                return info.nodeId;
        }
        return "";
    }

    public String getNodeIdFromClientAddr(String ip, int port) {
        String addr = ip + ":" + port;
        return getNodeIdFromClientAddr(addr);
    }

    public String[] getNodeIdsFromNodeType(String nodeType) {
        ArrayList<String> list = new ArrayList<String>();
        for(NodeInfo info : nodeMap.values()) {
            if(info.nodeType.equals(nodeType))
                list.add(info.nodeId);
        }
        return list.toArray(new String[0]);
    }

    public String getNodeType(String nodeId) {
        NodeInfo info = nodeMap.get(nodeId);
        if(info == null) return "";
        return info.nodeType;
    }

    public String getServerAddr(String nodeId) {
        NodeInfo info = nodeMap.get(nodeId);
        if(info == null) return "";
        return info.serverAddr;
    }

    public String getServerIp(String nodeId) {
        String addr = getServerAddr(nodeId);

        if(addr == null || addr.equals(""))
            return "";
        
        String[] info = addr.split(":");
        if(info == null || info.length != 2)
            return "";
        return info[0];
    }

    public int getServerPort(String nodeId) {
        String addr = getServerAddr(nodeId);

        if(addr == null || addr.equals(""))
            return 0;
        
        String[] info = addr.split(":");
        if(info == null || info.length != 2)
            return 0;
        return Integer.parseInt(info[1]);
    }

    public String getClientAddr(String nodeId) {
        NodeInfo info = nodeMap.get(nodeId);
        if(info == null) return "";
        return info.clientAddr;
    }

    public String getClientIp(String nodeId) {
        String addr = getClientAddr(nodeId);
        if(addr == null || addr.equals(""))
            return "";
        
        String[] info = addr.split(":");
        if(info == null || info.length != 2)
            return "";
        return info[0];        
    }

    public int getClientPort(String nodeId) {
        String addr = getClientAddr(nodeId);
        if(addr == null || addr.equals(""))
            return 0;
        
        String[] info = addr.split(":");
        if(info == null || info.length != 2)
            return 0;
        return Integer.parseInt(info[1]);
    }

    public String ipportToAddr(String ip, int port) {
        return ip + ":" + port;
    }

    public String ipFromAddr(String addr) {
        String[] info = addr.split(":");
        if(info == null || info.length != 2)
            return "";
        return info[0];
    }

    public int portFromAddr(String addr) {
        String[] info = addr.split(":");
        if(info == null || info.length != 2)
            return 0;
        return Integer.parseInt(info[1]);
    }
}