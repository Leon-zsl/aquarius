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

    private Node owner = null;
    private ClientBootstrap bootstrap = null;

    private ClientConnListener connListener = null;
    private ConcurrentHashMap<String, Channel> conns = new ConcurrentHashMap<String, Channel>();

    public Client(Node owner) {
        this.owner = owner;
    }

    public ClientConnListener getConnListener() { return this.connListener; }

    public Channel getChannel(String ip, int port) {
        String addr = ip + ":" + port;
        return conns.get(addr);
    }

    public synchronized void start(ClientConnListener l) {
        logger.info("start client...");
        this.connListener = l;
        ClientBootstrap bootstrap = new ClientBootstrap(
            new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                                              Executors.newCachedThreadPool()));
        this.bootstrap = bootstrap;
    }

    public synchronized void close() {
        logger.info("close client...");

        for(Channel conn : this.conns.values()) {
            conn.close();
        }
        this.conns.clear();

        if(this.bootstrap != null) {
            this.bootstrap.releaseExternalResources();
            this.bootstrap = null;
        }
    }

    public synchronized void connect(String ip, int port) {
        if(ip == null || ip.equals("") || port == 0)
            return;

        String addr = ip + ":" + port;
        if(conns.containsKey(addr)) {
            logger.info("node exists:" + addr);
            return;
        }

        logger.info("connect new node:" + "[ip]" + ip + "[port]" + port);

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
        Channel channel = future.awaitUninterruptibly().getChannel();
        this.conns.putIfAbsent(addr, channel);
    }

    public synchronized void disconnect(String ip, int port) {
        if(ip == null || ip.equals("") || port == 0)
            return;
        String addr = ip + ":" + port;
        Channel ch = this.conns.get(addr);
        if(ch != null) {
            ch.close();
            this.conns.remove(addr);
        }
    }

    public void nodeConnected(Channel ch) {
        if(this.connListener == null) return;
        
        InetSocketAddress addr = (InetSocketAddress)ch.getRemoteAddress();
        this.connListener.nodeConnected(addr.getHostString(),
                                        addr.getPort());
    }

    public void nodeDisconnected(Channel ch) {
        if(this.connListener == null) return;

        InetSocketAddress addr = (InetSocketAddress)ch.getRemoteAddress();
        String key = addr.getHostString() + ":" + addr.getPort();
        Channel channel = this.conns.get(key);
        if(channel == null) return;

        this.connListener.nodeDisconnected(addr.getHostString(),
                                           addr.getPort());
        channel.close();
        this.conns.remove(key);
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
