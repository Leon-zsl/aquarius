package com.leonc.zodiac.aquarius.world;

import com.leonc.zodiac.aquarius.base.event.Dispatcher;
import com.leonc.zodiac.aquarius.base.packet.PckDispatcher;

public abstract class Module
{
	protected volatile boolean running = false;
	
	protected Dispatcher dispatcher = new Dispatcher();
	protected PckDispatcher pckDispatcher = new PckDispatcher();
	
	public Dispatcher getDispatcher() { return this.dispatcher; }
	public PckDispatcher getPckDispatcher() { return this.pckDispatcher; }
	
	public void startup() {}
	public void shutdown() {}
}
