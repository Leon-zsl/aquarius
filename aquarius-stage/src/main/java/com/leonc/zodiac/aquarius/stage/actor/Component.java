package com.leonc.zodiac.aquarius.stage.actor;

public abstract class Component
{
	private Actor owner = null;
	private boolean isExclusive = false;
	
	public Actor getOwner() { return this.owner; }
	
	public boolean getExclusive() { return this.isExclusive; }
	public void setExclusive(boolean val) { this.isExclusive = val; }
	
	public Component(Actor owner) {
		this.owner = owner;
	}
	
	public void start() {}
	public void close() {}
	public void update() {}
}
