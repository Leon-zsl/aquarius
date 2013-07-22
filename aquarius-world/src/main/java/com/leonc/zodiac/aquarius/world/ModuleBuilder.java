package com.leonc.zodiac.aquarius.world;

import com.leonc.zodiac.aquarius.world.guild.GuildMgr;
import com.leonc.zodiac.aquarius.world.repository.RepositoryMgr;
import com.leonc.zodiac.aquarius.world.trade.TradeMgr;

public class ModuleBuilder
{
	public static void build(World w) {
		Module m = new GuildMgr();
		m.startup();
		w.addModule(m);
		
		m = new TradeMgr();
		m.startup();
		w.addModule(m);
		
		m = new RepositoryMgr();
		m.startup();
		w.addModule(m);
	}
}
