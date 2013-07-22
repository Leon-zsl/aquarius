package com.leonc.zodiac.aquarius.world;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.base.event.Dispatcher;
import com.leonc.zodiac.aquarius.base.packet.PckDispatcher;

public class World
{
	private static Log logger = LogFactory.getLog(World.class);
	
	private ConcurrentHashMap<String, Player> playerMap = new ConcurrentHashMap<String, Player>();
	private ArrayList<Module> modules = new ArrayList<Module>();
	
	private Dispatcher dispatcher = new Dispatcher();
	private PckDispatcher pckDispatcher = new PckDispatcher();
	
	public Dispatcher getDispatcher() { return this.dispatcher; }
	public PckDispatcher getPckDispatcher() { return this.pckDispatcher; }
	
	public void start() {
		ModuleBuilder.build(this);
	}
	
	public void close() {
		playerMap.clear();
		for(Module m : modules) {
			m.shutdown();
		}
		modules.clear();
	}
	
	public void update() {
		dispatcher.dispatch();
		pckDispatcher.dispatch();
	}
	
	public Player getPlayer(String sid) {
		return playerMap.get(sid);
	}
	
	public void addPlayer(Player p) {
		if(p == null) return;
		playerMap.put(p.getSid(), p);
	}
	
	public void delPlayer(String sid) {
		playerMap.remove(sid);
	}
	
	public Module getModule(int idx) {
		return modules.get(idx);
	}
	
	public Module getModule(Class cls) {
		for(Module m : modules) {
			if(m.getClass().equals(cls))
				return m;
		}
		return null;
	}
	
	public void addModule(Module mn) {
		for(Module m : modules) {
			if(m.getClass().equals(mn.getClass())) {
				logger.warn("module exists lready");
				return;
			}
		}
		modules.add(mn);
	}
	
	public void delModule(Module m) {
		modules.remove(m);
	}
}
