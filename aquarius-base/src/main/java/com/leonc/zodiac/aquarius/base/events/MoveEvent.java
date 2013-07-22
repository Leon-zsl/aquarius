package com.leonc.zodiac.aquarius.base.events;

import com.leonc.zodiac.aquarius.base.event.Event;
import com.leonc.zodiac.aquarius.base.util.Vector2;

public class MoveEvent extends Event {
	private Vector2 pos = Vector2.zero;
	
	public MoveEvent(Vector2 p) {
		this.pos = p;
	}
	
	public Vector2 getPos() { return this.pos; }
}
