package com.leonc.zodiac.aquarius.stage.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.base.message.MsgPacket;
import com.leonc.zodiac.aquarius.base.packet.Packet;
import com.leonc.zodiac.aquarius.base.packet.PacketWrapper;
import com.leonc.zodiac.aquarius.base.rpc.Command;
import com.leonc.zodiac.aquarius.base.rpc.Service;
import com.leonc.zodiac.aquarius.stage.App;
import com.leonc.zodiac.aquarius.stage.Stage;
import com.leonc.zodiac.aquarius.stage.StageMgr;

public class PacketService implements Service
{
	private static Log logger = LogFactory.getLog(PacketService.class);
	
	public void forward(Command cmd) {
		MsgPacket.C2SPacket msg = (MsgPacket.C2SPacket)cmd.getMessage();
		MsgPacket.Packet mp = msg.getPacket();
		String sid = msg.getSid();
		Packet pck = new Packet(mp.getOpcode(), mp.getData().toByteArray());
		
		StageMgr mgr = App.getInstance().getStageMgr();
		Stage stage = mgr.getStage(mgr.getRouter().get(sid));
		if(stage == null) {
			logger.warn("unknown sid for stage: " + sid);
			return;
		}
		
		stage.getPckDispatcher().sendPacket(new PacketWrapper(sid, pck));
	}
}
