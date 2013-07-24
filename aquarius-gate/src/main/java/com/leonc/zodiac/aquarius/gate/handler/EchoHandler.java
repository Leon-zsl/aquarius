package com.leonc.zodiac.aquarius.gate.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.base.packet.Packet;
import com.leonc.zodiac.aquarius.base.packet.PacketWrapper;
import com.leonc.zodiac.aquarius.gate.App;

public class EchoHandler implements Handler 
{
	private static Log logger = LogFactory.getLog(EchoHandler.class);
	
	public void handle(PacketWrapper pw) {
		logger.info("recv pck: [op]" + pw.getPacket().getOpcode() + 
					" [msg]" + new String(pw.getPacket().getData()));
		
		App.getInstance().getAcceptor().sendPacket(pw.getSid(), 
				new Packet(2, "this is server".getBytes()));
	}
}
