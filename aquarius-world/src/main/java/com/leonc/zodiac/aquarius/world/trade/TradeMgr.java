package com.leonc.zodiac.aquarius.world.trade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.world.Global;
import com.leonc.zodiac.aquarius.world.Module;

public class TradeMgr extends Module
{
	private static Log logger = LogFactory.getLog(TradeMgr.class);
	
	public void startup() {
		this.running = true;
		Thread th = new Thread(new TradeImpl(this));
		th.start();
	}
	
	public void shutdown() {
		this.running = false;
	}
	
	public class TradeImpl implements Runnable {
		private TradeMgr owner;
		public TradeImpl(TradeMgr owner) {
			this.owner = owner;
		}
		
		public void run() {
			this.start();
			while(this.owner.running) {
				try {
					long t = System.currentTimeMillis();
					this.update();
					long dt = System.currentTimeMillis() - t;
					if(Global.FRAME_TIME - dt > 0)
						Thread.sleep(Global.FRAME_TIME - dt);
				} catch(Exception ex) {
					logger.warn("guild except: " + ex);
				}
			}
			this.close();
		}
		
		private void start() {
			
		}
		
		private void close() {
			
		}
		
		private void update() {
			this.owner.dispatcher.dispatch();
			this.owner.pckDispatcher.dispatch();
		}
	}
}
