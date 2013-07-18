package com.leonc.zodiac.aquarius.base.rpc;

import java.net.InetSocketAddress;
import java.lang.reflect.*;
import java.lang.Class;

import org.jboss.netty.channel.Channel;
import com.google.protobuf.Message;

public class Command
{
    private String remoteIp;
    private int remotePort;
    private Message message;

    private Packet packet;
    private Channel channel;

    public Command() {
        this.packet = null;
        this.channel = null;

        this.remoteIp = "";
        this.remotePort = 0;
        this.message = null;
    }

    public Command(Packet pck, Channel ch) throws Exception {
        this.packet = pck;
        this.channel = ch;

        InetSocketAddress addr = (InetSocketAddress)ch.getRemoteAddress();
        this.remoteIp = addr.getHostString();
        this.remotePort = addr.getPort();

        String clsName = pck.getMessageName();
        Class cls = Class.forName(clsName);
        Method instance = cls.getDeclaredMethod("getDefaultInstance", null);
        Message proto = (Message)instance.invoke(null, null);
        Message msg = proto.newBuilderForType().mergeFrom(pck.getMessageData()).build();
        this.message = msg;
    }

    public Channel getChannel() { return this.channel; }
    public Command setChannel(Channel ch) { this.channel = ch; return this; }

    public Packet getPacket() { return this.packet; }
    public Command setPacket(Packet pck) { this.packet = pck; return this; }

    public Message getMessage() { return this.message; }
    public Command setMessage(Message msg) { this.message = msg; return this; }

    public String getRemoteIp() { return this.remoteIp; }
    public Command setRemoteIp(String ip) { this.remoteIp = ip; return this; }

    public int getRemotePort() { return this.remotePort; }
    public Command setRemotePort(int port) { this.remotePort = port; return this; }

    public String getServiceName() { return this.packet.getServiceName(); }
    public String getMethodName() { return this.packet.getMethodName(); }
}
