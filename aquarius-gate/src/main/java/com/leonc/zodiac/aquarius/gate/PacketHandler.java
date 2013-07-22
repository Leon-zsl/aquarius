package com.leonc.zodiac.aquarius.gate;

import com.google.protobuf.ByteString;
import com.leonc.zodiac.aquarius.base.message.MsgPacket;
import com.leonc.zodiac.aquarius.base.packet.Packet;
import com.leonc.zodiac.aquarius.base.proto.TargetMap;

public class PacketHandler implements Runnable {
	private String sid = "";
	private Packet packet = null;
	
	public PacketHandler(String sid, Packet pck) {
		this.sid = sid;
		this.packet = pck;
	}
	
	public void run() {
		//route the packet to target server
		if(packet == null) return;
		if(sid == null || sid.equals("")) return;
		
		String target = TargetMap.dic.get(packet.getOpcode());
		String nodeId = PlayerMap.getNodeID(sid, target);
		
		MsgPacket.Packet pck = MsgPacket.Packet.newBuilder().
			setOpcode(this.packet.getOpcode()).
			setData(ByteString.copyFrom(this.packet.getData())).build();
		MsgPacket.C2SPacket msg = MsgPacket.C2SPacket.newBuilder().
			setSid(this.sid).setPacket(pck).build();
		App.getInstance().getNode().remoteCall(nodeId, "PacketService", "forward", msg);
	}
}
