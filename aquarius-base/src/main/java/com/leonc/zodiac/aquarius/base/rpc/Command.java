package com.leonc.zodiac.aquarius.base.rpc;

public class Command
{
    private String remoteId;
    private String serviceName;
    private byte[] args;

    public Command() {
        this.remoteId = "";
        this.serviceName = "";
        this.args = null;
    }

    public Command(String remoteId, String serviceName, byte[] args) {
        this.remoteId = remoteId;
        this.serviceName = serviceName;
        this.args = args;
    }

    public String getRemoteId() { return this.remoteId; }

    public Command setRemoteId(String id) { 
        this.remoteId = id;
        return this;
    }

    public String getServiceName() { return this.serviceName; }

    public Command setServiceName(String name) {
        this.serviceName = name;
        return this;
    }

    public byte[] getArgs() { return this.args; }

    public Command setArgs(byte[] args) {
        this.args = args;
        return this;
    }
}
