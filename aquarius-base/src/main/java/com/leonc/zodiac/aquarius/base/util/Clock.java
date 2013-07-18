package com.leonc.zodiac.aquarius.base.util;

/**this clock must be called in one thread
 */
public class Clock
{
    private long curtime;
    private long pretime;
    private long dttime;

    private boolean running;

    public void start() {
        reset();
        running = true;
    }

    public void stop() {
        reset();
        running = false;
    }

    public void reset() {
        curtime = System.currentTimeMillis();
        pretime = System.currentTimeMillis();
        dttime = 0;
    }

    public long frameTime() { return curtime; }
    public long deltaTime() { return dttime; }

    public void update() {
        if(!running) return;

        curtime = System.currentTimeMillis();
        dttime = curtime - pretime;
        if(dttime < 0) {
            dttime = 30;//default value
        }
        pretime = curtime;
    }
}
