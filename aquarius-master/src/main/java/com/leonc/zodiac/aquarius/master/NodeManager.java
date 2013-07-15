package com.leonc.zodiac.aquarius.master;

import com.leonc.zodiac.aquarius.base.util.Clock;
import com.leonc.zodiac.aquarius.base.rpc.Node;

import java.util.concurrent.ConcurrentHashMap;

public class NodeManager
{
    private Clock clock = new Clock();

    private ConcurrentHashMap<String, Node> nodeMap = new ConcurrentHashMap<String, Node>();

    public void start() {
        //do nothing
    }
    
    public void close() {
    	//do nothing
    }

    public void update() {
        //do nothing
    }

    public void nodeJoinIn(String nodeId) {
    	//todo:
    }
    
    public void getNodeList() {
    	//todo:
    }
    
    public void heartbeat(String nodeId) {
    	//todo:
    }
    
    private void checkDeathNode() {
    	//todo:
    }
}