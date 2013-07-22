package com.leonc.zodiac.aquarius.world.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.base.message.MsgPacket;
import com.leonc.zodiac.aquarius.base.packet.Packet;
import com.leonc.zodiac.aquarius.base.packet.PacketWrapper;
import com.leonc.zodiac.aquarius.base.rpc.Command;
import com.leonc.zodiac.aquarius.base.rpc.Service;
import com.leonc.zodiac.aquarius.world.App;
import com.leonc.zodiac.aquarius.world.Global;
import com.leonc.zodiac.aquarius.world.guild.GuildMgr;
import com.leonc.zodiac.aquarius.world.repository.RepositoryMgr;
import com.leonc.zodiac.aquarius.world.trade.TradeMgr;

public class PacketService implements Service
{
	private static Log logger = LogFactory.getLog(PacketService.class);
	
	public void forward(Command cmd) {
		MsgPacket.C2SPacket msg = (MsgPacket.C2SPacket)cmd.getMessage();
		MsgPacket.Packet mp = msg.getPacket();
		String sid = msg.getSid();
		Packet pck = new Packet(mp.getOpcode(), mp.getData().toByteArray());
		
		PacketWrapper pw = new PacketWrapper(sid, pck);
		if(isGuildPacket(pck.getOpcode())) {
			App.getInstance().getWorld().getModule(GuildMgr.class).getPckDispatcher().sendPacket(pw);
		} else if(isTradePacket(pck.getOpcode())) {
			App.getInstance().getWorld().getModule(TradeMgr.class).getPckDispatcher().sendPacket(pw);
		} else if(isRepositoryPacket(pck.getOpcode())) {
			App.getInstance().getWorld().getModule(RepositoryMgr.class).getPckDispatcher().sendPacket(pw);
		} else {
			App.getInstance().getWorld().getPckDispatcher().sendPacket(pw);
		}
	}
	
	private boolean isGuildPacket(int op) {
		return op >= Global.GUILD_OP_MIN && op <= Global.GUILD_OP_MAX;
	}
	
	private boolean isTradePacket(int op) {
		return op >= Global.TRADE_OP_MIN && op <= Global.TRADE_OP_MAX;
	}
	
	private boolean isRepositoryPacket(int op) {
		return op >= Global.REPOSITORY_OP_MIN && op <= Global.REPOSITORY_OP_MAX;
	}
}
