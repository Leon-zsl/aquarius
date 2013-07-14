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

    public String getSender() { return this.sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getReceiver() { return this.receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public String getService() { return this.service; }
    public void setService(String service) { this.service = service; }

    public byte[] getArgs() { return this.args; }
    public void setArgs(byte[] args) { this.args = args; }

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
