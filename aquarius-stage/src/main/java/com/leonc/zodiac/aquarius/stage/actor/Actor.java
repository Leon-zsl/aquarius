package com.leonc.zodiac.aquarius.stage.actor;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.stage.Stage;

public class Actor
{
	private static Log logger = LogFactory.getLog(Actor.class);
	
	private Stage owner = null;
	private String uuid = UUID.randomUUID().toString();
	private ArrayList<Component> compList = new ArrayList<Component>();
	
	public String getUuid() { return this.uuid; }
	
	public Actor(Stage owner) {
		this.owner = owner;
	}
	
	public void addComp(Component comp) {
		if(comp == null) return;
		
		if(comp.getExclusive()) {
			for(Component c : compList) {
				if(c.getClass().equals(comp.getClass())) {
					logger.info("duplicate comp to add: " + comp.getClass());
					return;
				}
			}
		}
		
		compList.add(comp);
	}
	
	public void delComp(Component comp) {
		compList.remove(comp);
	}
	
	public Component[] getComponents() {
		return compList.toArray(new Component[0]);
	}
	
	public Component getComponent(int idx) {
		return compList.get(idx);
	}
	
	public void start() {
		
	}
	
	public void close() {
		
	}
	
	public void update() {
		
	}
}