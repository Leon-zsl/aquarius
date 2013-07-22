package com.leonc.zodiac.aquarius.base.util;

public final class Vector2
{
	public final static Vector2 zero = new Vector2();
	public final static Vector2 one = new Vector2(1, 1);
	
	private float x;
	private float y;
	
	public float getX() { return this.x; }
	public void setX(float x) { this.x = x; }
	
	public float getY() { return this.y; }
	public void setY(float y) { this.y = y; }
	
	public Vector2() { this.x = 0; this.y = 0; }
	public Vector2(float x, float y) { this.x = x; this.y = y; }
	public Vector2(Vector2 v) { this.x = v.x; this.y = v.y; }
	
	public static Vector2 add(Vector2 v0, Vector2 v1) {
		return new Vector2(v0.x + v1.x, v0.y + v1.y);
	}
	
	public static Vector2 scale(Vector2 v, float s) {
		return new Vector2(v.x * s, v.y * s);
	}
	
	public static Vector2 sub(Vector2 v0, Vector2 v1) {
		return new Vector2(v0.x - v1.x, v0.y - v1.y);
	}
	
	public void add(Vector2 v) { this.x += v.x; this.y += v.y; }
	
	public void sub(Vector2 v) { this.x -= v.x; this.y -= v.y; }
	
	public void scale(float s) { this.x *= s; this.y *= s; }
}
