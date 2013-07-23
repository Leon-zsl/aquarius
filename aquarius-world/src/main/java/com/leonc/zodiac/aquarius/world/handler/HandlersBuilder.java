package com.leonc.zodiac.aquarius.world.handler;

import com.leonc.zodiac.aquarius.world.World;
import com.leonc.zodiac.aquarius.world.guild.GuildMgr;
import com.leonc.zodiac.aquarius.world.repository.RepositoryMgr;
import com.leonc.zodiac.aquarius.world.trade.TradeMgr;

public class HandlersBuilder {
	public static void build(World w) {
		WorldHandlers.build(w);
		TradeHandlers.build((TradeMgr)w.getModule(TradeMgr.class));
		RepositoryHandlers.build((RepositoryMgr)w.getModule(RepositoryMgr.class));
		GuildHandlers.build((GuildMgr)w.getModule(GuildMgr.class));
	}
}
