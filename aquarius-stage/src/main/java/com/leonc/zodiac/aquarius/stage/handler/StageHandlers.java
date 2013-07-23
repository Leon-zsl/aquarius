package com.leonc.zodiac.aquarius.stage.handler;

import com.leonc.zodiac.aquarius.base.packet.Handler;
import com.leonc.zodiac.aquarius.base.packet.PacketWrapper;
import com.leonc.zodiac.aquarius.base.proto.ProtoID;
import com.leonc.zodiac.aquarius.stage.Stage;

public class StageHandlers {
	public static void build(Stage s) {
		s.getPckDispatcher().registerHandler(ProtoID.ENTERSTAGE, new EnterHandler(s));
		s.getPckDispatcher().registerHandler(ProtoID.LEAVESTAGE, new LeaveHandler(s));
	}

	public static class EnterHandler implements Handler {
		private Stage stage;
		
		public EnterHandler(Stage stage) {
			this.stage = stage;
		}
		
		public void handle(PacketWrapper pw) {
			
		}
	}
	
	public static class LeaveHandler implements Handler {
		private Stage stage;
		
		public LeaveHandler(Stage stage) {
			this.stage = stage;
		}
		
		public void handle(PacketWrapper pw) {
			
		}
	}
}