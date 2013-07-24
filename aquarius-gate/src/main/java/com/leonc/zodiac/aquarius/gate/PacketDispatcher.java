package com.leonc.zodiac.aquarius.gate;

import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.ByteString;
import com.leonc.zodiac.aquarius.base.message.MsgPacket;
import com.leonc.zodiac.aquarius.base.packet.Packet;
import com.leonc.zodiac.aquarius.base.packet.PacketWrapper;
import com.leonc.zodiac.aquarius.base.proto.TargetMap;
import com.leonc.zodiac.aquarius.gate.handler.Handler;
import com.leonc.zodiac.aquarius.gate.handler.HandlerBuilder;

public class PacketDispatcher implements Runnable {
	private String sid = "";
	private Packet packet = null;
	
	private ConcurrentHashMap<Integer, Handler> handlerMap = new ConcurrentHashMap<Integer, Handler>();
	
	public PacketDispatcher(String sid, Packet pck) {
		this.sid = sid;
		this.packet = pck;
		
		HandlerBuilder.build(this);
	}
	
	public void registerHandler(int op, Handler h) {
		handlerMap.put(op, h);
	}
	
	public void unregister(int op) {
		handlerMap.remove(op);
	}

	public void run() {
		//route the packet to target server
		if(packet == null) return;
		if(sid == null || sid.equals("")) return;
		
		//handle packet local
		Handler h = handlerMap.get(packet.getOpcode());
		if(h != null) {
			h.handle(new PacketWrapper(sid, packet));
			return;
		}
		
		//route the pck to other node
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
