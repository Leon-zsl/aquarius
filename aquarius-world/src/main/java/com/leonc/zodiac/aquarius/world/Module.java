package com.leonc.zodiac.aquarius.world;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.base.event.Dispatcher;
import com.leonc.zodiac.aquarius.base.packet.PckDispatcher;
import com.leonc.zodiac.aquarius.base.util.Clock;

public abstract class Module implements Runnable
{
	protected static Log logger = LogFactory.getLog(Module.class);
	
	protected volatile boolean running = false;
	
	protected Clock clock = new Clock();
	protected Dispatcher dispatcher = new Dispatcher();
	protected PckDispatcher pckDispatcher = new PckDispatcher();
	
	public Clock getClock() { return this.clock; }
	public Dispatcher getDispatcher() { return this.dispatcher; }
	public PckDispatcher getPckDispatcher() { return this.pckDispatcher; }
	
	public void shutdown() { this.running = false; }
	
	public void run() {
		this.start();
		while(this.running) {
			try {
				long t = System.currentTimeMillis();
				this.update();
				long dt = System.currentTimeMillis() - t;
				if(Global.FRAME_TIME - dt > 0)
					Thread.sleep(Global.FRAME_TIME - dt);
			} catch(Exception ex) {
				logger.warn("module except: " + ex);
			}
		}
		this.close();
	}
	
	protected void start() { this.running = true; this.clock.start(); }
	protected void close() { this.running = false; this.clock.stop(); }
	protected void update() {
		this.clock.update();
		this.dispatcher.dispatch();
		this.pckDispatcher.dispatch();
	}
}
