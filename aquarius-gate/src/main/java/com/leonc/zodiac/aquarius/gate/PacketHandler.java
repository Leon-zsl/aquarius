package com.leonc.zodiac.aquarius.gate;

import com.google.protobuf.ByteString;
import com.leonc.zodiac.aquarius.base.message.MsgPacketForward;
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
		
		MsgPacketForward.Packet pck = MsgPacketForward.Packet.newBuilder().
			setOpcode(this.packet.getOpcode()).
			setData(ByteString.copyFrom(this.packet.getData())).build();
		MsgPacketForward.C2SPacket msg = MsgPacketForward.C2SPacket.newBuilder().
			setSid(this.sid).setPacket(pck).build();
		App.getInstance().getNode().remoteCall(nodeId, "PacketForward", "forwardPacket", msg);
	}
}
