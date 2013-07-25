package com.leonc.zodiac.aquarius.base.packet;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PckDispatcher
{
	private HashMap<Integer, Handler> handlers = new HashMap<Integer, Handler>();
    private ConcurrentLinkedQueue<PacketWrapper> pckQ = new ConcurrentLinkedQueue<PacketWrapper>();

    private static Log logger = LogFactory.getLog(PckDispatcher.class);

    public void registerHandler(int opcode, Handler h) {
        if(h == null) return;
        handlers.put(opcode, h);
    }

    public void unregisterHandler(int op) {
        handlers.remove(op);
    }

    public void sendPacket(PacketWrapper pw) {
        if(pw == null) return;
        pckQ.add(pw);
    }

    public void dispatch() {
        while(true) {
            PacketWrapper pw = pckQ.poll();
            if(pw == null) break;

            Handler h = handlers.get(pw.getPacket().getOpcode());
            if(h == null) {
            	logger.warn("unknown pck: " + pw.getPacket().getOpcode());
            	continue;
            }
            h.handle(pw);
        }
    }
}
