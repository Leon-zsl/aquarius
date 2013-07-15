package com.leonc.zodiac.aquarius.base.rpc;

import com.google.protobuf.Message;

public class Command
{
    private String remoteId;
    private String serviceName;
    private String methodName;
    private Message message;

    public Command() {
        this.remoteId = "";
        this.serviceName = "";
        this.methodName = "";
        this.message = null;
    }

    public Command(String remoteId, String serviceName, String methodName, Message message) {
        this.remoteId = remoteId;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.message = message;
    }

    public String getRemoteId() { return this.remoteId; }
    public Command setRemoteId(String id) { this.remoteId = id; return this; }

    public String getServiceName() { return this.serviceName; }
    public Command setServiceName(String name) { this.serviceName = name; return this; }

    public String getMethodName() { return this.methodName; }
    public Command setMethodName(String name) { this.methodName = name; return this; }

    public Message getMessage() { return this.message; }
    public Command setMessage(Message msg) { this.message = msg; return this; }
}
