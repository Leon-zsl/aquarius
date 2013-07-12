package com.leonc.zodiac.aquarius.base.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * this is single thread version of event dispatcher
 */
public class Dispatcher
{
    private HashMap<String, ArrayList<Handler>> handlers = new HashMap<String, ArrayList<Handler>>();
    private ConcurrentLinkedQueue<Event> eventQ = new ConcurrentLinkedQueue<Event>();

    private static Log logger = LogFactory.getLog(Dispatcher.class);

    public void registerHandler(String cmd, Handler h) {
        if(cmd == null || cmd.equals("")) return;
        if(h == null) return;

        if(handlers.containsKey(cmd)) {
            handlers.get(cmd).add(h);
        } else {
            ArrayList<Handler> l = new ArrayList<Handler>();
            l.add(h);
            handlers.put(cmd, l);
        }
    }

    public void unregisterHandler(String cmd, Handler h) {
        if(cmd == null || cmd.equals("")) return;
        if(h == null) return;

        if(handlers.containsKey(cmd)) {
            handlers.get(cmd).remove(h);
            if(handlers.get(cmd).isEmpty()) {
                handlers.remove(cmd);
            }
        }
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
            ArrayList<Handler> list = handlers.get(cmd);
            if(list == null) continue;

            for(Handler h : list) {
                logger.debug("handle event: " + cmd);
                h.Handle(e);
            }
        }
    }
}
