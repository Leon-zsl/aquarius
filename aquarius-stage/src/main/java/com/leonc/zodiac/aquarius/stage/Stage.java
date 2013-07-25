package com.leonc.zodiac.aquarius.stage;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.base.event.Dispatcher;
import com.leonc.zodiac.aquarius.base.packet.PckDispatcher;
import com.leonc.zodiac.aquarius.base.util.Clock;
import com.leonc.zodiac.aquarius.stage.actor.Actor;
import com.leonc.zodiac.aquarius.stage.handler.HandlersBuilder;
import com.leonc.zodiac.aquarius.stage.quest.QuestMgr;
import com.leonc.zodiac.aquarius.stage.scene.Scene;

//one thread one stage
public class Stage implements Runnable
{
	private static Log logger = LogFactory.getLog(Stage.class);

	private volatile boolean running = false;

	private String uuid = UUID.randomUUID().toString();
	private Clock clock = new Clock();
	private Dispatcher dispatcher = new Dispatcher();
	private PckDispatcher pckDispatcher = new PckDispatcher();
	private Scene scene = new Scene();
	private ConcurrentHashMap<String, Actor> actorMap = new ConcurrentHashMap<String, Actor>();
	private QuestMgr questMgr = new QuestMgr();
	
	public String getUuid() { return this.uuid; }
	public Scene getScene() { return this.scene; }
	public Clock getClock() { return this.clock; }
	public Dispatcher getDispatcher() { return this.dispatcher; }
	public PckDispatcher getPckDispatcher() { return this.pckDispatcher; }
	public QuestMgr getQuestMgr() { return this.questMgr; }
	
	public Stage() {
		HandlersBuilder.build(this);
	}
	
	public Actor getActor(String uuid) {
		return this.actorMap.get(uuid);
	}
	
	public Collection<Actor> getActors() {
		return actorMap.values();
	}
	
	public void addActor(Actor act) {
		if(act == null) return;
		actorMap.put(act.getUuid(), act);
	}
	
	public void delActor(String uuid) {
		actorMap.remove(uuid);
	}

	public void shutdown() {
		this.running = false;
	}
	
	public void run() {
		this.start();
		while(this.running) {
			try {
				long t = System.currentTimeMillis();
				this.update();
				long dt = System.currentTimeMillis() - t;
				if(Global.STAGE_FRAME_TIME - dt > 0)
					Thread.sleep(Global.STAGE_FRAME_TIME - dt);
			} catch (Exception ex) {
				logger.error("stage except: " + uuid + ":" + ex);
			}
		}
		this.close();
	}
	
	private void start() {
		this.running = true;
		this.clock.start();
		this.scene.start();
		this.questMgr.start();
	}
	
	private void close() {
		for(Actor act : this.actorMap.values()) {
			act.close();
		}
		this.scene.close();
		this.questMgr.close();
	}
	
	private void update() {
		this.clock.update();
		this.pckDispatcher.dispatch();
		this.dispatcher.dispatch();
		this.scene.update();
		this.questMgr.update();
		for(Actor act : this.actorMap.values()) {
			act.update();
		}
	}
}
