package com.leonc.zodiac.aquarius.base.packet;

public class PacketWrapper
{
	private String sid = "";
	private Packet packet = null;
	
	public PacketWrapper(String sid, Packet pck) {
		this.sid = sid;
		this.packet = pck;
	}
	
	public String getSid() { return this.sid; }
	public Packet getPacket() { return this.packet; }
}
