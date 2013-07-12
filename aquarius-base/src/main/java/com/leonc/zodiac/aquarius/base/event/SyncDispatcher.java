package com.leonc.zodiac.aquarius.base.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * this is thread safe version of event dispatcher
 */
public class SyncDispatcher
{
    private ReentrantLock handlesLock = new ReentrantLock();
    private HashMap<String, ArrayList<Handler>> handlers = new HashMap<String, ArrayList<Handler>>();

    private ConcurrentLinkedQueue<Event> eventQ = new ConcurrentLinkedQueue<Event>();

    private static Log logger = LogFactory.getLog(SyncDispatcher.class);

    public void registerHandler(String cmd, Handler h) {
        if(cmd == null || cmd.equals("")) return;
        if(h == null) return;

        handlesLock.lock();
        if(handlers.containsKey(cmd)) {
            handlers.get(cmd).add(h);
        } else {
            ArrayList<Handler> l = new ArrayList<Handler>();
            l.add(h);
            handlers.put(cmd, l);
        }
        handlesLock.unlock();
    }

    public void unregisterHandler(String cmd, Handler h) {
        if(cmd == null || cmd.equals("")) return;
        if(h == null) return;

        handlesLock.lock();
        if(handlers.containsKey(cmd)) {
            handlers.get(cmd).remove(h);
            if(handlers.get(cmd).isEmpty()) {
                handlers.remove(cmd);
            }
        }
        handlesLock.unlock();        
    }

    public void sendEvent(Event e) {
        if(e == null) return;
        eventQ.add(e);
    }

    public void dispatch() {
        while(true) {
            Event e = eventQ.poll();
            if(e == null) break;

            String cmd = e.getCmd();

            handlesLock.lock();
            ArrayList<Handler> list = handlers.get(cmd);
            handlesLock.unlock();
            
            if(list == null) continue;

            for(Handler h : list) {
                logger.debug("handle event: " + cmd);
                h.Handle(e);
            }
        }
    }
}
