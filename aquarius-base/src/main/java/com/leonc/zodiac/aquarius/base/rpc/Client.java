package com.leonc.zodiac.aquarius.base.rpc;

public final class Client
{
    private String targetName = "";
    private String targetType = "";

    private String targetIP = "";
    private int targetPort = 0;

    public Client(String targetName, String targetType) {
        this.targetName = targetName;
        this.targetType = targetType;
    }

    public void connect(String ip, int port) {
        this.targetIP = ip;
        this.targetPort = port;

        //todo:
    }

    public void close() {
        this.targetIP = "";
        this.targetPort = 0;

        //todo:
    }

    public void sendRequest(String serviceName, Packet pck) {
        //todo:
    }
}
