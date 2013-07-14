package com.leonc.zodiac.aquarius.base.rpc;

public class Node 
{
    private Server server = null;
    private Client client = null;

    public Server getServer() { return this.server; }
    public Client getClient() { return this.client; }

    public void initServer(String id, int port) {
        server = new Server(this, id, port);
        server.start();
    }

    public void initClient() {
        client = new Client(this);
        client.start();
    }
}
