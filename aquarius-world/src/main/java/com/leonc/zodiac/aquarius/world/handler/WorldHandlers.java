package com.leonc.zodiac.aquarius.world.handler;

import com.leonc.zodiac.aquarius.base.packet.Handler;
import com.leonc.zodiac.aquarius.base.packet.PacketWrapper;
import com.leonc.zodiac.aquarius.base.proto.ProtoID;
import com.leonc.zodiac.aquarius.world.World;

public class WorldHandlers
{
	public static void build(World w) {
		w.getPckDispatcher().registerHandler(ProtoID.LOGIN, new LoginHandler(w));
		w.getPckDispatcher().registerHandler(ProtoID.LOGOUT, new LogoutHandler(w));
		w.getPckDispatcher().registerHandler(ProtoID.SWITCHSTAGE, new EnterStageHandler(w));
	}
	
	public static class LoginHandler implements Handler {
		private World owner;
		
		public LoginHandler(World w) {
			this.owner = w;
		}
		
		public void handle(PacketWrapper pw) {
			//todo:
		}
	}
	
	public static class LogoutHandler implements Handler {
		private World owner;
		
		public LogoutHandler(World w) {
			this.owner = w;
		}
		
		public void handle(PacketWrapper pw) {
			//todo:
		}
	}
	
	public static class EnterStageHandler implements Handler {
		private World owner;
		
		public EnterStageHandler(World w) {
			this.owner = w;
		}
		
		public void handle(PacketWrapper pw) {
			//todo:
		}
	}
}
