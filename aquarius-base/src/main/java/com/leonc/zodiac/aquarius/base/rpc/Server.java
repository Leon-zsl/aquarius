package com.leonc.zodiac.aquarius.base.rpc;

public final class Server {
    private String name;
    private String type;

    private String ip = "";
    private int port = 0;

    public void listen(String name, String type, String ip, int port) {
        this.name = name;
        this.type = type;
        this.ip = ip;
        this.port = port;

        //todo:
    }

    public void registerService(String name, Service sv) {
        //todo:
    }

    public void unregisterService(String name) {
        //todo:
    }

    public void dispatchService() {
        //todo:
    }
}
