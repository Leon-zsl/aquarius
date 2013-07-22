package com.leonc.zodiac.aquarius.gate;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PlayerMap 
{
	private static Log logger = LogFactory.getLog(PlayerMap.class);
	
	private static ConcurrentHashMap<String, String> stageDic = new ConcurrentHashMap<String, String>();
	
	public static String getNodeID(String sid, String target) {
		String[] nodeIds = App.getInstance().getNode().getRouter().getNodeIdsFromNodeType(target);
		if(nodeIds == null || nodeIds.length == 0) {
			logger.warn("unknown node type: " + target);
			return "";
		}
		
		if(nodeIds.length == 1) {
			return nodeIds[0];
		}
		
		//nodeids.length > 1
		if(target == "stage") {
			return stageDic.get(sid);
		} else {
			logger.warn("do not support multi target now");
			return "";
		}
	}
	
	public static void setPlayerStageServer(String sid, String nodeId) {
		stageDic.put(sid, nodeId);
	}
}
