package com.leonc.zodiac.aquarius.base.util;

/**this clock must be called in one thread
 */
public class Clock
{
    private long curtime;
    private long pretime;
    private long dttime;

    public void start() {
        curtime = System.currentTimeMillis();
        pretime = System.currentTimeMillis();
        dttime = 0;
    }

    public long frameTime() { return curtime; }
    public long deltaTime() { return dttime; }

    public void update() {
        curtime = System.currentTimeMillis();
        dttime = curtime - pretime;
        if(dttime < 0) {
            dttime = 30;//default value
        }
        pretime = curtime;
    }
}
