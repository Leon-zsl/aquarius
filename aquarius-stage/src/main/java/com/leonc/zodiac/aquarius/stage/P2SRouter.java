package com.leonc.zodiac.aquarius.stage;

import java.util.concurrent.ConcurrentHashMap;

public class P2SRouter 
{
	//ssid -> stage id
	private ConcurrentHashMap<String, String> playerToStageMap = new ConcurrentHashMap<String, String>();
	
	public void set(String pid, String sid) {
		this.playerToStageMap.put(pid, sid);
	}
	
	public String get(String pid) {
		return this.playerToStageMap.get(pid);
	}
}
