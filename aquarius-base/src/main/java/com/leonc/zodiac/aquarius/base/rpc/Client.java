package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

//import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Client
{
    private static Log logger = LogFactory.getLog(Client.class);

    private ClientBootstrap bootstrap = null;

    private ClientConnListener connListener = null;
    private ConcurrentHashMap<String, Channel> conns = new ConcurrentHashMap<String, Channel>();
    //remote server channel addr -> remote listen addr
    private ConcurrentHashMap<String, String> addrMap = new ConcurrentHashMap<String, String>();

    public ClientConnListener getConnListener() { return this.connListener; }
    public Client setConnListener(ClientConnListener l) {
        this.connListener = l;
        return this;
    }

    public Channel getChannel(String listenIp, int listenPort) {
        String addr = listenIp + ":" + listenPort;
        return conns.get(addr);
    }

    public synchronized void start() {
        logger.info("start client...");
        ClientBootstrap bootstrap = new ClientBootstrap(
            new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                                              Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ClientPipelineFactory(this));
        this.bootstrap = bootstrap;
    }

    public synchronized void close() {
        logger.info("close client...");

        this.addrMap.clear();

        for(Channel conn : this.conns.values()) {
            conn.close();
        }
        this.conns.clear();

        if(this.bootstrap != null) {
            this.bootstrap.releaseExternalResources();
            this.bootstrap = null;
        }
    }

    public synchronized void connect(String listenIp, int listenPort) {
        if(listenIp == null || listenIp.equals("") || listenPort == 0)
            return;

        String addr = listenIp + ":" + listenPort;
        if(conns.containsKey(addr)) {
            logger.info("node exists:" + addr);
            return;
        }

        logger.info("connect new node:" + "[ip]" + listenIp + "[port]" + listenPort);

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(listenIp, listenPort));
        Channel channel = future.awaitUninterruptibly().getChannel();
        this.conns.putIfAbsent(addr, channel);

        InetSocketAddress chsock = (InetSocketAddress)channel.getRemoteAddress();
        String chaddr = chsock.getHostString() + ":" + chsock.getPort();
        this.addrMap.putIfAbsent(chaddr, addr);
    }

    public synchronized void disconnect(String listenIp, int listenPort) {
        if(listenIp == null || listenIp.equals("") || listenPort == 0)
            return;
        String addr = listenIp + ":" + listenPort;
        Channel ch = this.conns.get(addr);
        if(ch != null) {
            ch.close();
            this.conns.remove(addr);
        }

        for(String chaddr : this.addrMap.keySet()) {
            if(addrMap.get(chaddr) == addr) {
                addrMap.remove(chaddr);
                break;
            }
        }
    }

    public void nodeConnected(Channel ch) {
        if(this.connListener == null) return;
        
        InetSocketAddress addr = (InetSocketAddress)ch.getRemoteAddress();
        this.connListener.nodeConnected(addr.getHostString(),
                                        addr.getPort());
    }

    public void nodeDisconnected(Channel ch) {
        InetSocketAddress chsock = (InetSocketAddress)ch.getRemoteAddress();
        String chaddr = chsock.getHostString() + ":" + chsock.getPort();
        String addr = addrMap.get(chaddr);
        
        Channel channel = this.conns.get(addr);
        if(channel == null) return;
        
        if(this.connListener != null) {
        	String[] info = addr.split(":");
        	this.connListener.nodeDisconnected(info[0], Integer.parseInt(info[1]));
        }
        
        channel.close();
        this.conns.remove(addr);
        this.addrMap.remove(chaddr);
    }

    public void remoteCall(String ip, int port, String serviceName, 
                           String methodName, Message msg) {
        if(ip == null || ip == "" || port == 0)
            return;

        String addr = ip + ":" + port;
        Channel ch = conns.get(addr);
        if(ch != null) {
            Packet pck = new Packet();

            pck.setServiceName(serviceName);
            pck.setMethodName(methodName);

            pck.setMessageName(msg.getClass().getName());
            pck.setMessageData(msg.toByteArray());

            ch.write(pck);
        }
    }

    public void broadcast(String serviceName, String methodName, Message msg) {
        for(Channel ch : conns.values()) {
            if(ch == null) continue;

            Packet pck = new Packet();

            pck.setServiceName(serviceName);
            pck.setMethodName(methodName);

            pck.setMessageName(msg.getClass().getName());
            pck.setMessageData(msg.toByteArray());

            ch.write(pck);            
        }
    }

    public void broadcastToOthers(String excludeIp, int excludePort, 
                                  String serviceName, String methodName, 
                                  Message msg) {
        String targetAddr = excludeIp + ":" + excludePort;
        for(String addr : conns.keySet()) {
            if(addr.equals(targetAddr)) continue;

            Channel ch = conns.get(addr);
            if(ch == null) continue;

            Packet pck = new Packet();

            pck.setServiceName(serviceName);
            pck.setMethodName(methodName);

            pck.setMessageName(msg.getClass().getName());
            pck.setMessageData(msg.toByteArray());

            ch.write(pck);            
        }        
    }
}
