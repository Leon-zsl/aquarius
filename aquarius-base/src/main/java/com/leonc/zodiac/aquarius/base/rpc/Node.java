package com.leonc.zodiac.aquarius.base.rpc;

import java.util.concurrent.ConcurrentHashMap;

public class Node 
{
    private Server server = new Server();
    private ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap<String, Client>();

    public void startServer(String name, String type, String ip, int port) {
        //todo:
    }

    public void connectToNode(String nameName, String type, String ip, int port) {
        //todo:
    }

    public void disconnectFrom(String name) {
        //todo:
    }
}
