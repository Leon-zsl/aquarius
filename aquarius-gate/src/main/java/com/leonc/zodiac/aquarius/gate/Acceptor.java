package com.leonc.zodiac.aquarius.gate;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leonc.zodiac.aquarius.base.packet.Packet;

public class Acceptor
{
    private static Log logger = LogFactory.getLog(Acceptor.class);

    private String listenIp = "";
    private int listenPort = 0;
    private Channel channel = null;
    private ServerBootstrap bootstrap = null;
    
    private ExecutorService threadPool = null;
    private ConcurrentHashMap<String, Channel> chMap = new ConcurrentHashMap<String, Channel>();

    public void start(int port) {
    	String ip = "127.0.0.1";
    	try {
    		ip = InetAddress.getLocalHost().getHostAddress();
    	} catch(Exception ex) {
    		logger.error("acceptor start err: " + ex);
    	}
    	this.start(ip, port);
    }
    
    public void start(String ip, int port) {
    	this.threadPool = Executors.newCachedThreadPool();
    	
        ServerBootstrap bootstrap = new ServerBootstrap(
            new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                                              Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new GatePipelineFactory(this));
        Channel ch = bootstrap.bind(new InetSocketAddress(ip, port));

        InetSocketAddress addr = (InetSocketAddress)ch.getLocalAddress();
        this.listenIp = addr.getHostString();
        this.listenPort = addr.getPort();
        this.channel = ch;
        this.bootstrap = bootstrap;
    }

    public void close() {
    	for(Channel ch : chMap.values()) {
    		ch.close();
    	}
    	chMap.clear();
    	
        if(this.channel != null) {
            this.channel.close();
            this.channel = null;
        }

        if(this.bootstrap != null) {
            this.bootstrap.releaseExternalResources();
            this.bootstrap = null;
        }
        
        threadPool.shutdown();
    }
    
    public void addChannel(String sid, Channel ch) {
    	if(ch == null) return;
    	this.chMap.put(sid, ch);
    }
    
    public void delChannel(String sid) {
    	Channel ch = this.chMap.remove(sid);
    	if(ch != null) ch.close();
    }
   
    public void broadcast(Packet pck) {
    	for(Channel ch : chMap.values()) {
    		ch.write(pck);
    	}
    }
    
    public void broadcast(String[] sids, Packet pck) {
    	for(String sid : sids) {
    		Channel ch = chMap.get(sid);
    		if(ch == null) {
    			logger.warn("unknown sid: " + sid);
    			continue;
    		}
    		ch.write(pck);
    	}
    }
    
    public void sendPacket(String sid, Packet pck) {
    	Channel ch = chMap.get(sid);
    	if(ch == null) {
    		logger.warn("unknown sid: " + sid);
    		return;
    	}
    	ch.write(pck);
    }
    
    public void recvPacket(String sid, Packet pck) {
    	if(pck == null) return;
    	
    	this.threadPool.execute(new PacketDispatcher(sid, pck));
    }
}