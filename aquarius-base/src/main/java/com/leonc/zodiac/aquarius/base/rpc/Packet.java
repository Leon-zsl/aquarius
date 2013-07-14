package com.leonc.zodiac.aquarius.base.rpc;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Packet {
	private static Log logger = LogFactory.getLog(Packet.class);
	
    private String sender = "";
    private String receiver = "";
    private String service = "";
    private byte[] args = null;

    public Packet() {
        this.sender = "";
        this.receiver = "";
        this.service = "";
        this.args = null;
    }

    public Packet(String sender, String receiver, String service, byte[] args) {
        this.sender = sender;
        this.receiver = receiver;
        this.service = service;
        this.args = args;
    }

    public String getSender() { return this.sender; }
    public Packet setSender(String sender) { this.sender = sender; return this; }

    public String getReceiver() { return this.receiver; }
    public Packet setReceiver(String receiver) { this.receiver = receiver; return this; }

    public String getService() { return this.service; }
    public Packet setService(String service) { this.service = service; return this; }

    public byte[] getArgs() { return this.args; }
    public Packet setArgs(byte[] args) { this.args = args; return this; }

    public void encode(ChannelBuffer buf) {
        Charset set = Charset.forName("utf-8");

        byte[] sb = this.sender.getBytes(set);
        buf.writeShort((short)sb.length);
        buf.writeBytes(sb);

        byte[] rb = this.receiver.getBytes(set);
        buf.writeShort((short)rb.length);
        buf.writeBytes(rb);

        byte[] svb = this.service.getBytes(set);
        buf.writeShort((short)svb.length);
        buf.writeBytes(svb);

        if(this.args == null) {
            buf.writeInt(0);
        } else {
            buf.writeInt(this.args.length);
            buf.writeBytes(this.args);
        }
    }

    public void decode(ChannelBuffer buf) {
        String set = "utf-8";

       try {
	        short len = buf.readShort();
	        byte[] tmp = new byte[len];
	        buf.readBytes(tmp);
	        this.sender = new String(tmp, set);
	
	        len = buf.readShort();
	        tmp = new byte[len];
	        buf.readBytes(len);
	        this.receiver = new String(tmp, set);
	
	        len = buf.readShort();
	        tmp = new byte[len];
	        buf.readBytes(len);
	        this.service = new String(tmp, set);
	
	        int l = buf.readInt();
	        this.args = new byte[l];
	        buf.readBytes(this.args);
       } catch(UnsupportedEncodingException e) {
    	   logger.error("packet decode exception" + e);
       }
    }
}
