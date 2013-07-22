package com.leonc.zodiac.aquarius.gate.service;

import com.leonc.zodiac.aquarius.base.message.MsgPacket;
import com.leonc.zodiac.aquarius.base.packet.Packet;
import com.leonc.zodiac.aquarius.base.rpc.Service;
import com.leonc.zodiac.aquarius.base.rpc.Command;
import com.leonc.zodiac.aquarius.gate.Acceptor;
import com.leonc.zodiac.aquarius.gate.PlayerMap;

public class RouteService implements Service 
{
	private Acceptor owner;

	public RouteService(Acceptor owner) {
		this.owner = owner;
	}
	
	public void sendTo(Command cmd) {
		MsgPacket.S2CPacket msg = (MsgPacket.S2CPacket)cmd.getMessage();
		int op = msg.getPacket().getOpcode();
		byte[] data = msg.getPacket().getData().toByteArray();
		this.owner.sendPacket(msg.getSid(), new Packet(op, data));
	}
	
	public void broadcast(Command cmd) {
		MsgPacket.S2APacket msg = (MsgPacket.S2APacket)cmd.getMessage();
		int op = msg.getPacket().getOpcode();
		byte[] data = msg.getPacket().getData().toByteArray();
		this.owner.broadcast(new Packet(op, data));
	}
	
	public void broadcastToAll(Command cmd) {
		MsgPacket.S2MPacket msg = (MsgPacket.S2MPacket)cmd.getMessage();
		int op = msg.getPacket().getOpcode();
		byte[] data = msg.getPacket().getData().toByteArray();
		String[] sids = msg.getSidList().toArray(new String[0]);
		this.owner.broadcast(sids, new Packet(op, data));
	}
	
	public void playerChangeStageServer(Command cmd) {
		MsgPacket.PlayerToServer msg = (MsgPacket.PlayerToServer)cmd.getMessage();
		PlayerMap.setPlayerStageServer(msg.getSid(), msg.getNodeId());
	}
}
