package com.leonc.zodiac.aquarius.gate.handler;

import com.leonc.zodiac.aquarius.gate.PacketDispatcher;

public class HandlerBuilder {
	public static void build(PacketDispatcher owner) {
		owner.registerHandler(1, new EchoHandler());
	}
}
