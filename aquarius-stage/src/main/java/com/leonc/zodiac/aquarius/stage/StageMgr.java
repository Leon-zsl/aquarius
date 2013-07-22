package com.leonc.zodiac.aquarius.stage;

import java.util.concurrent.ConcurrentHashMap;

public class StageMgr
{
	private P2SRouter router = new P2SRouter();
	
	//stage id -> stage
	private ConcurrentHashMap<String, Stage> stageMap = new ConcurrentHashMap<String, Stage>();

	public void start() {}
	
	public void close() {
		for(Stage s : stageMap.values()) {
			s.shutdown();
		}
		stageMap.clear();
	}
	
	public Stage createStage() {
		Stage st = new Stage();
		stageMap.put(st.getUuid(), st);
		st.startup();
		return st;
	}
	
	public void destroyStage(String uuid) {
		Stage st = stageMap.get(uuid);
		if(st != null) {
			st.shutdown();
			stageMap.remove(uuid);
		}
	}
	
	public Stage getStage(String uuid) { return stageMap.get(uuid); }
	
	public int getStageCount() { return stageMap.size(); }
	
	public P2SRouter getRouter() { return this.router; }
}
