package com.leonc.zodiac.aquarius.gate.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.base.packet.Packet;
import com.leonc.zodiac.aquarius.base.packet.PacketWrapper;
import com.leonc.zodiac.aquarius.gate.App;

import com.leonc.zodiac.aquarius.base.proto.ProtoEcho;

public class EchoHandler implements Handler 
{
	private static Log logger = LogFactory.getLog(EchoHandler.class);
	
	public void handle(PacketWrapper pw) {
		try {
			int op = pw.getPacket().getOpcode();
			byte[] data = pw.getPacket().getData();
			ProtoEcho.EchoMsg msg = ProtoEcho.EchoMsg.parseFrom(data);
			logger.info("recv pck: [op]" + op + 
						"[id]" + msg.getId() +
						"[data]" + msg.getData() +
						"[uid]" + msg.getUid() +
						"[info]" + msg.getInfo());
			
			ProtoEcho.EchoMsg sm = ProtoEcho.EchoMsg.newBuilder().
					setId(msg.getId() + 1).
					setData("this is server").
					setUid(msg.getUid() + 1).
					setInfo("this is response").build();
			App.getInstance().getAcceptor().sendPacket(pw.getSid(), 
					new Packet(2, sm.toByteArray()));
		} catch (Exception e) {
			logger.error("parse pck err: " + e);
		}
	}
}
