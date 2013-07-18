package com.leonc.zodiac.aquarius.base.rpc;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Packet {
	private static Log logger = LogFactory.getLog(Packet.class);
	
    private String serviceName = "";
    private String methodName = "";
    private String messageName = "";
    private byte[] messageData = null;

    public Packet() {
        this.serviceName = "";
        this.methodName = "";
        this.messageName = "";
        this.messageData = null;
    }

    public Packet(String serviceName, String methodName,
                  String messageName, byte[] messageData) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.messageName = messageName;
        this.messageData = messageData;
    }

    // public String getSender() { return this.sender; }
    // public Packet setSender(String sender) { this.sender = sender; return this; }

    // public String getReceiver() { return this.receiver; }
    // public Packet setReceiver(String receiver) { this.receiver = receiver; return this; }

    public String getServiceName() { return this.serviceName; }
    public Packet setServiceName(String name) { this.serviceName = name; return this; }

    public String getMethodName() { return this.methodName; }
    public Packet setMethodName(String name) { this.methodName = name; return this; }

    public String getMessageName() { return this.messageName; }
    public Packet setMessageName(String name) { this.messageName = name; return this; }

    public byte[] getMessageData() { return this.messageData; }
    public Packet setMessageData(byte[] data) { this.messageData = data; return this; }

    public void encode(ChannelBuffer buf) {
        Charset set = Charset.forName("utf-8");
        byte[] data = null;

        // byte[] data = this.sender.getBytes(set);
        // buf.writeShort((short)data.length);
        // buf.writeBytes(data);

        // data = this.receiver.getBytes(set);
        // buf.writeShort((short)data.length);
        // buf.writeBytes(data);

        data = this.serviceName.getBytes(set);
        buf.writeShort((short)data.length);
        buf.writeBytes(data);

        data = this.methodName.getBytes(set);
        buf.writeShort((short)data.length);
        buf.writeBytes(data);

        data = this.messageName.getBytes(set);
        buf.writeShort((short)data.length);
        buf.writeBytes(data);

        data = this.messageData;
        if(data == null) {
            buf.writeInt(0);
        } else {
            buf.writeInt(data.length);
            buf.writeBytes(data);
        }
    }

    public void decode(ChannelBuffer buf) {
        try {
            String set = "utf-8";

            short len = 0;
            byte[] tmp = null;

	        // short len = buf.readShort();
	        // byte[] tmp = new byte[len];
	        // buf.readBytes(tmp);
	        // this.sender = new String(tmp, set);
	
	        // len = buf.readShort();
	        // tmp = new byte[len];
	        // buf.readBytes(len);
	        // this.receiver = new String(tmp, set);
	
	        len = buf.readShort();
	        tmp = new byte[len];
	        buf.readBytes(len);
	        this.serviceName = new String(tmp, set);

            len = buf.readShort();
            tmp = new byte[len];
            buf.readBytes(tmp);
            this.methodName = new String(tmp, set);
	
            len = buf.readShort();
            tmp = new byte[len];
            buf.readBytes(tmp);
            this.messageName = new String(tmp, set);

	        int l = buf.readInt();
	        this.messageData = new byte[l];
	        buf.readBytes(this.messageData);
       } catch(UnsupportedEncodingException e) {
    	   logger.error("packet decode exception" + e);
       }
    }
}
